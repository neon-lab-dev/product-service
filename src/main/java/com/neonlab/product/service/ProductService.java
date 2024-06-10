package com.neonlab.product.service;
import com.neonlab.common.annotations.Loggable;
import com.neonlab.common.config.ConfigurationKeys;
import com.neonlab.common.constants.GlobalConstants;
import com.neonlab.common.entities.Document;
import com.neonlab.common.expectations.*;
import com.neonlab.common.models.PageableResponse;
import com.neonlab.common.services.DocumentService;
import com.neonlab.common.services.SystemConfigService;
import com.neonlab.common.services.UserService;
import com.neonlab.common.utilities.MathUtils;
import com.neonlab.common.utilities.PageableUtils;
import com.neonlab.common.utilities.StringUtil;
import com.neonlab.product.constants.EntityConstant;
import com.neonlab.product.dtos.ProductDto;
import com.neonlab.product.dtos.VarietyDto;
import com.neonlab.product.entities.Product;
import com.neonlab.product.entities.Variety;
import com.neonlab.product.models.responses.ProductReportModel;
import com.neonlab.product.models.responses.ProductVarietyResponse;
import com.neonlab.product.models.searchCriteria.ProductSearchCriteria;
import com.neonlab.product.repository.ProductRepository;
import com.neonlab.common.utilities.ObjectMapperUtils;
import com.neonlab.product.repository.VarietyRepository;
import com.neonlab.product.repository.specifications.ProductSpecifications;
import com.neonlab.product.repository.specifications.VarietySpecifications;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.math.BigDecimal;
import java.util.*;


@Service
@Loggable
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    public final static String BRAND = "non-branded";

    public final static String TAG = "Tags1";

    public final static String MESSAGE = "Product unique code already exists or This Product Already Exists";

    public final static String DELETE_MESSAGE = "Product deleted successfully.";

    public final static String WHOLE_PRODUCT_DELETE_MESSAGE = "Product Deleted Successfully";


    private final ProductRepository productRepository;
    private final DocumentService documentService;
    private final UserService userService;
    private final VarietyRepository varietyRepository;
    private final SystemConfigService systemConfigService;
    private final CategoryService categoryService;

    public ProductDto add(ProductDto productReqDto) throws ServerException, InvalidInputException {
        var product = saveAndMapDocument(productReqDto);
        var varieties = saveAndMapVarieties(product, productReqDto.getVarietyList());
        var retVal = ObjectMapperUtils.map(product, ProductDto.class);
        retVal.setVarietyList(varieties);
        retVal.setDocumentUrls(getDocumentUrls(product));
        return retVal;
    }


    private Product saveAndMapDocument(ProductDto productDto) throws ServerException, InvalidInputException {
        var retVal = ObjectMapperUtils.map(productDto, Product.class);
        setDefaultIfRequired(retVal);
        var user = userService.getLoggedInUser();
        retVal.setCreatedBy(user.getPrimaryPhoneNo());
        retVal.setCreatedAt(new Date());
        var product = productRepository.save(retVal);
        mapProductDocument(productDto, product);
        return product;
    }

    private void mapProductDocument(ProductDto productDto, Product product) throws ServerException {
        if (!CollectionUtils.isEmpty(productDto.getDocuments())){
            var documents = documentService.saveAll(productDto.getDocuments());
            for (var document : documents) {
                document.setDocIdentifier(product.getId());
                document.setEntityName(product.getClass().getSimpleName());
                documentService.save(document);
            }
            limitDocumentSize(product.getId(),product.getClass().getSimpleName());
        }
    }

    private void setDefaultIfRequired(Product product){
        if (Objects.nonNull(product.getBrand()) && product.getBrand().isEmpty()){
            product.setBrand(BRAND);
        }
        if (Objects.nonNull(product.getTags()) && product.getTags().isEmpty()){
            product.setTags(TAG);
        }
    }

    private List<VarietyDto> saveAndMapVarieties(Product product, List<VarietyDto> varieties) throws ServerException, InvalidInputException {
        var retVal = new ArrayList<VarietyDto>();
        for (var varietyDto : varieties){
            var variety = saveVariety(varietyDto,product);
            saveAndMapDocument(varietyDto, variety);
            var varDto = ObjectMapperUtils.map(variety, VarietyDto.class);
            varDto.setDocumentUrls(getDocumentUrls(variety,product));
            retVal.add(varDto);
        }
        return retVal;
    }


    private void saveAndMapDocument(VarietyDto varietyDto, Variety variety) throws ServerException {
        if (!CollectionUtils.isEmpty(varietyDto.getDocuments())){
            var documents = documentService.saveAll(varietyDto.getDocuments());
            for (var document : documents) {
                document.setDocIdentifier(variety.getId());
                document.setEntityName(variety.getClass().getSimpleName());
                documentService.save(document);
            }

            limitDocumentSize(variety.getId(),variety.getClass().getSimpleName());
        }
    }

    private void limitDocumentSize(String id,String entityName) throws ServerException {
        var documentList = documentService.fetchByDocIdentifierAndEntityNameAsc(id, entityName);
        if (documentList.size() > 4) {
            documentService.maintainSize(documentList,4);
        }
    }

    private Variety saveVariety(VarietyDto varietyDto, Product product) throws ServerException, InvalidInputException {
        var entity = ObjectMapperUtils.map(varietyDto, Variety.class);
        var user = userService.getLoggedInUser();
        entity.setCreatedBy(user.getPrimaryPhoneNo());
        entity.setCreatedAt(new Date());
        entity.setModifiedAt(new Date());
        entity.setProduct(product);
        if(Objects.isNull(entity.getDiscountPercent())){
            entity.setDiscountPercent(BigDecimal.valueOf(0));
        }
        if(Objects.isNull(entity.getDiscountPrice())){
            entity.setDiscountPrice(BigDecimal.ZERO);
        }
        entity.setDiscountPercent(MathUtils.getDiscountPercent(entity.getPrice(),entity.getDiscountPrice()));
        return varietyRepository.save(entity);
    }

    public boolean existingProduct(String code) {
        return productRepository.findByCode(code).isPresent();
    }

    @Transactional
    public ProductDto update(ProductDto product) throws ServerException, InvalidInputException {
       var productEntity = fetchById(product.getId());
       updateDocumentIfRequired(product,productEntity);
       ObjectMapperUtils.map(product, productEntity);
       productEntity = productRepository.save(productEntity);
       var varieties = new ArrayList<VarietyDto>();
       if(!CollectionUtils.isEmpty(product.getVarietyList())) {
           for (var dto : product.getVarietyList()) {
               var varietyEntity = fetchVarietyById(dto.getId());
               ObjectMapperUtils.map(dto, varietyEntity);
               varietyEntity.setDiscountPercent(MathUtils.getDiscountPercent(varietyEntity.getPrice(),varietyEntity.getDiscountPrice()));
               varietyEntity = varietyRepository.save(varietyEntity);
               updateDocumentIfRequired(dto, varietyEntity);
               var varietyDto = ObjectMapperUtils.map(varietyEntity, VarietyDto.class);
               varietyDto.setDocumentUrls(getDocumentUrls(varietyEntity,productEntity));
               varieties.add(varietyDto);
           }
       }
       var retVal = ObjectMapperUtils.map(productEntity, ProductDto.class);
       retVal.setVarietyList(varieties);
       retVal.setDocumentUrls(getDocumentUrls(productEntity));
       return retVal;
    }

    private void updateDocumentIfRequired(ProductDto product, Product productEntity) throws InvalidInputException, ServerException {
        if(!CollectionUtils.isEmpty(product.getDocuments())){
            mapProductDocument(product,productEntity);
        }
    }

    private void updateDocumentIfRequired(VarietyDto varietyDto, Variety variety) throws ServerException {
        if (!CollectionUtils.isEmpty(varietyDto.getDocuments())){
            saveAndMapDocument(varietyDto, variety);
        }
    }


    public String deleteProduct(List<String> productIds) throws  InvalidInputException {
        for (var productId : productIds){
            var product = productRepository.findById(productId);
            if (product.isPresent()){
                product.get().getVarieties().forEach(this::deleteAllVarietyDocuments);
                deleteAllProductDocuments(product.get());
                productRepository.delete(product.get());
            }
        }
        return DELETE_MESSAGE;
    }

    public String deleteWholeProduct(Product product) {
        productRepository.delete(product);
        return WHOLE_PRODUCT_DELETE_MESSAGE;
    }

    public Product fetchProductByCode(String code) throws InvalidInputException {
        return productRepository.findByCode(code)
                .orElseThrow(() -> new InvalidInputException("Product not found with code "+code));
    }

    public PageableResponse<ProductDto> fetchProducts(final ProductSearchCriteria searchCriteria){
        if (!StringUtil.isNullOrEmpty(searchCriteria.getSortByProductField())){
            searchCriteria.setSortBy(searchCriteria.getSortByProductField());
        }
        var pageable = PageableUtils.createPageable(searchCriteria);
        var productCodes = productRepository.findAll(
                ProductSpecifications.buildSearchCriteria(searchCriteria),
                pageable
        ).getContent().stream().map(Product::getCode).toList();
        searchCriteria.setSortBy(GlobalConstants.CREATED_AT);
        searchCriteria.setCodes(productCodes);
        if (!StringUtil.isNullOrEmpty(searchCriteria.getSortByVarietyField())){
            searchCriteria.setSortBy(searchCriteria.getSortByVarietyField());
        }
        var varieties = varietyRepository.findAll(
                VarietySpecifications.buildSearchCriteria(searchCriteria),
                Sort.by(searchCriteria.getSortDirection(), searchCriteria.getSortBy())
        );
        var resultList = fetchProductDto(varieties);
        return new PageableResponse<>(resultList, searchCriteria);
    }

    private List<ProductDto> fetchProductDto(List<Variety> varieties){
        var retVal = new ArrayList<ProductDto>();
        if (!CollectionUtils.isEmpty(varieties)){
            var productVarietyMap = new LinkedHashMap<String, List<VarietyDto>>();
            varieties.forEach(variety -> constructProductIdToVarietyDtoListMap(productVarietyMap, variety));
            for (var entry : productVarietyMap.entrySet()){
                retVal.add(constructProductDto(entry.getKey(), entry.getValue()));
            }
        }
        return retVal;
    }

    private ProductDto constructProductDto(String productId, List<VarietyDto> varietyDtos){
        try {
            var product = fetchById(productId);
            var dto = ObjectMapperUtils.map(product, ProductDto.class);
            dto.setDocumentUrls(getDocumentUrls(product));
            dto.setVarietyList(varietyDtos);
            return dto;
        } catch (ServerException | InvalidInputException e) {
            log.warn(e.getMessage());
        }
        return null;
    }


    private void constructProductIdToVarietyDtoListMap(Map<String, List<VarietyDto>> retVal, Variety variety) {
        try {
            var product = variety.getProduct();
            var value = retVal.getOrDefault(product.getId(), new ArrayList<>());
            var dto = ObjectMapperUtils.map(variety, VarietyDto.class);
            dto.setDocumentUrls(getDocumentUrls(variety,product));
            value.add(dto);
            retVal.put(product.getId(), value);
        } catch (ServerException e) {
            log.warn(e.getMessage());
        }
    }

    private List<String> getDocumentUrls(Variety variety, Product product){
        var docs = documentService.fetchByDocIdentifierAndEntityName(variety.getId(), EntityConstant.Variety.ENTITY_NAME);
        if(!CollectionUtils.isEmpty(docs)) {
            return docs.stream()
                    .map(Document::getUrl)
                    .toList();
        }
        return getDocumentUrls(product);
    }

    private List<String> getDocumentUrls(Product product) {
        var productDoc = documentService.fetchByDocIdentifierAndEntityName(product.getId(), EntityConstant.Product.ENTITY_NAME);
        return productDoc.stream()
                .map(Document::getUrl)
                .toList();
    }

    private void deleteAllProductDocuments(Product product){
        var docs = getDocuments(product.getId(), EntityConstant.Product.ENTITY_NAME);
        docs.forEach(doc -> {
            try {
                documentService.delete(doc);
            } catch (ServerException ignored) {}
        });
    }

    private void deleteAllVarietyDocuments(Variety variety){
        var docs = getDocuments(variety.getId(), EntityConstant.Variety.ENTITY_NAME);
        docs.forEach(doc -> {
            try {
                documentService.delete(doc);
            } catch (ServerException ignored) {}
        });
    }

    private List<Document> getDocuments(String docIdentifier, String entityName){
        return documentService.fetchByDocIdentifierAndEntityName(docIdentifier, entityName);
    }

    /**
     * Gives the product variety response from the variety
     *
     * @param variety variety entity
     * @return Product Variety Response
     * @throws InvalidInputException in case invalid variety details
     */
    public ProductVarietyResponse fetchProductVarietyResponse(Variety variety) throws InvalidInputException {
        var product = fetchById(variety.getProduct().getId());
        var documentUrls = getDocumentUrls(variety, variety.getProduct());
        var config = systemConfigService.getSystemConfig(ConfigurationKeys.DELIVERY_CHARGE);
        var deliveryCharge = BigDecimal.valueOf(Integer.parseInt(config.getValue()));
        return ProductVarietyResponse.buildByProductVarietyAndDocuments()
                .product(product)
                .variety(variety)
                .documents(documentUrls)
                .deliveryCharges(deliveryCharge)
                .build();
    }

    private Product fetchById(String id) throws InvalidInputException {
        var retVal = productRepository.findById(id);
        if (retVal.isPresent()){
            return retVal.get();
        }
        throw new InvalidInputException("Product not found with Id "+id);
    }

    public Variety fetchVarietyById(String id) throws InvalidInputException {
        return varietyRepository.findById(id)
                .orElseThrow(() -> new InvalidInputException("Variety not found with id "+id));
    }

    public Variety saveVariety(final Variety variety){
        return varietyRepository.save(variety);
    }

    public ProductReportModel getReport(){
        var totalProducts = productRepository.count();
        var outOfStockCount = productRepository.count(
                ProductSpecifications.buildSearchCriteria(
                        ProductSearchCriteria.productSearchCriteriaBuilder()
                                .quantity(0)
                                .build()
                ));
        return new ProductReportModel(totalProducts, outOfStockCount);
    }

    public void validateCategory(ProductDto productDto) throws InvalidInputException {
        if(!categoryService.isExistingCategory(productDto.getCategory())){
            throw new InvalidInputException("category does not exist with name "+productDto.getCategory());
        }
        if(!categoryService.isExistingCategory(productDto.getSubCategory())){
            throw new InvalidInputException("subCategory does not exist with name "+productDto.getSubCategory());
        }
        if(!categoryService.isExistingCategory(productDto.getSubCategory2())){
            throw new InvalidInputException("subCategory2 does not exist with name "+productDto.getSubCategory2());
        }

    }

}

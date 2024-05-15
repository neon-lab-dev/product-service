package com.neonlab.product.service;
import com.neonlab.common.annotations.Loggable;
import com.neonlab.common.config.ConfigurationKeys;
import com.neonlab.common.entities.Document;
import com.neonlab.common.expectations.*;
import com.neonlab.common.models.PageableResponse;
import com.neonlab.common.services.DocumentService;
import com.neonlab.common.services.SystemConfigService;
import com.neonlab.common.services.UserService;
import com.neonlab.common.utilities.MathUtils;
import com.neonlab.common.utilities.PageableUtils;
import com.neonlab.product.dtos.ProductDto;
import com.neonlab.product.dtos.VarietyDto;
import com.neonlab.product.entities.Product;
import com.neonlab.product.entities.Variety;
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

    public ProductDto add(ProductDto productReqDto) throws ServerException, InvalidInputException {
        var product = saveAndMapDocument(productReqDto);
        var varieties = saveAndMapVarieties(product, productReqDto.getVarietyList());
        var retVal = ObjectMapperUtils.map(product, ProductDto.class);
        retVal.setVarietyList(varieties);
        return retVal;
    }

    private Product saveAndMapDocument(ProductDto productDto) throws ServerException, InvalidInputException {
        var retVal = ObjectMapperUtils.map(productDto, Product.class);
        setDefaultIfRequired(retVal);
        var user = userService.getLoggedInUser();
        retVal.setCreatedBy(user.getPrimaryPhoneNo());
        retVal.setCreatedAt(new Date());
        var product = productRepository.save(retVal);
        if (!CollectionUtils.isEmpty(productDto.getDocuments())){
            var documents = documentService.saveAll(productDto.getDocuments());
            for (var document : documents) {
                document.setDocIdentifier(product.getId());
                document.setEntityName(product.getClass().getSimpleName());
                documentService.save(document);
            }
            var documentList = documentService.fetchByDocIdentifierAndEntityNameAsc(product.getId(), product.getClass().getSimpleName());
            if (documentList.size() > 4) {
                documentService.maintainSize(documentList);
            }
        }
        return product;
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
            retVal.add(ObjectMapperUtils.map(variety, VarietyDto.class));
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
            var documentList = documentService.fetchByDocIdentifierAndEntityNameAsc(variety.getId(), variety.getClass().getSimpleName());
            if (documentList.size() > 4) {
                documentService.maintainSize(documentList);
            }
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
        return varietyRepository.save(entity);
    }

    public boolean existingProduct(String code) {
        return productRepository.findByCode(code).isPresent();
    }

    @Transactional
    public ProductDto update(ProductDto product) throws ServerException, InvalidInputException {
       var productEntity = fetchById(product.getId());
       ObjectMapperUtils.map(product, productEntity);
       productEntity = productRepository.save(productEntity);
       var varieties = new ArrayList<VarietyDto>();
       for (var dto : product.getVarietyList()){
           var varietyEntity = fetchVarietyById(dto.getId());
           ObjectMapperUtils.map(dto, varietyEntity);
           varietyEntity = varietyRepository.save(varietyEntity);
           updateDocumentIfRequired(dto, varietyEntity);
           varieties.add(ObjectMapperUtils.map(varietyEntity, VarietyDto.class));
       }
       var retVal = ObjectMapperUtils.map(productEntity, ProductDto.class);
       retVal.setVarietyList(varieties);
       return retVal;
    }

    private void updateDocumentIfRequired(VarietyDto varietyDto, Variety variety) throws ServerException {
        if (!CollectionUtils.isEmpty(varietyDto.getDocuments())){
            saveAndMapDocument(varietyDto, variety);
        }
    }


    public String deleteProduct(List<String> productIds) throws  InvalidInputException {
        for (var productId : productIds){
            var product = productRepository.findById(productId);
            product.ifPresent(value -> productRepository.delete(product.get()));
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
        var pageable = PageableUtils.createPageable(searchCriteria);
        var productCodes = productRepository.findAll(
                ProductSpecifications.buildSearchCriteria(searchCriteria),
                pageable
        ).getContent().stream().map(Product::getCode).toList();
        searchCriteria.setCodes(productCodes);
        var varieties = varietyRepository.findAll(
                VarietySpecifications.buildSearchCriteria(searchCriteria)
        );
        var resultList = fetchProductDto(varieties);
        return new PageableResponse<>(resultList, searchCriteria);
    }

    private List<ProductDto> fetchProductDto(List<Variety> varieties){
        var retVal = new ArrayList<ProductDto>();
        if (!CollectionUtils.isEmpty(varieties)){
            var productVarietyMap = new HashMap<String, List<VarietyDto>>();
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
            dto.setVarietyList(varietyDtos);
            return dto;
        } catch (ServerException | InvalidInputException e) {
            log.warn(e.getMessage());
        }
        return null;
    }


    private void constructProductIdToVarietyDtoListMap(Map<String, List<VarietyDto>> retVal, Variety variety) {
        try {
            var productId = variety.getProduct().getId();
            var value = retVal.getOrDefault(productId, new ArrayList<>());
            var dto = ObjectMapperUtils.map(variety, VarietyDto.class);
            dto.setDiscountPrice(MathUtils.getDiscountedPrice(dto.getPrice(), dto.getDiscountPercent()));
            var docIds = getDocumentIds(variety);
            dto.setDocumentUrls(docIds);
            value.add(dto);
            retVal.put(productId, value);
        } catch (ServerException e) {
            log.warn(e.getMessage());
        }
    }

    private List<String> getDocumentIds(Variety variety){
        var docs = documentService.fetchByDocIdentifierAndEntityName(variety.getId(), variety.getClass().getSimpleName());
        return docs.stream()
                .map(Document::getId)
                .toList();
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
        var documents = documentService.fetchByDocIdentifierAndEntityName(variety.getId(), variety.getClass().getSimpleName());
        var documentIds = documents.stream()
                .map(Document::getId)
                .toList();
        var config = systemConfigService.getSystemConfig(ConfigurationKeys.DELIVERY_CHARGE);
        var deliveryCharge = BigDecimal.valueOf(Integer.parseInt(config.getValue()));
        return ProductVarietyResponse.buildByProductVarietyAndDocuments()
                .product(product)
                .variety(variety)
                .documents(documentIds)
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

}

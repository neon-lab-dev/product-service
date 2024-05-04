package com.neonlab.product.service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neonlab.common.annotations.Loggable;
import com.neonlab.common.entities.Document;
import com.neonlab.common.expectations.*;
import com.neonlab.common.services.BoundedQueue;
import com.neonlab.common.services.DocumentService;
import com.neonlab.common.services.UserService;
import com.neonlab.common.utilities.PageableUtils;
import com.neonlab.product.dtos.BoughtProductDetailsDto;
import com.neonlab.product.dtos.ProductDto;
import com.neonlab.product.dtos.VarietyDto;
import com.neonlab.product.entities.Order;
import com.neonlab.product.entities.Product;
import com.neonlab.product.entities.Variety;
import com.neonlab.product.models.responses.PageableResponse;
import com.neonlab.product.models.responses.ProductVarietyResponse;
import com.neonlab.product.models.searchCriteria.ProductSearchCriteria;
import com.neonlab.product.repository.ProductRepository;
import com.neonlab.common.utilities.ObjectMapperUtils;
import com.neonlab.product.repository.VarietyRepository;
import com.neonlab.product.repository.specifications.VarietySpecifications;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;


@Service
@Loggable
@Slf4j
public class ProductService {

    public final static String BRAND = "non-branded";

    public final static String TAG = "Tags1";

    public final static String MESSAGE = "Product unique code already exists or This Product Already Exists";

    public final static String DELETE_MESSAGE = "Product deleted successfully.";

    public final static String WHOLE_PRODUCT_DELETE_MESSAGE = "Product Deleted Successfully";

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private UserService userService;

    @Autowired
    private VarietyRepository varietyRepository;


    public ProductDto add(ProductDto productReqDto) throws ServerException, InvalidInputException {
        var product = save(productReqDto);
        var varieties = saveAndMapVarieties(product, productReqDto.getVarieties());
        var retVal = ObjectMapperUtils.map(product, ProductDto.class);
        retVal.setVarieties(varieties);
        return retVal;
    }

    private Product save(ProductDto productDto) throws ServerException, InvalidInputException {
        var retVal = ObjectMapperUtils.map(productDto, Product.class);
        setDefaultIfRequired(retVal);
        var user = userService.getLoggedInUser();
        retVal.setCreatedBy(user.getPrimaryPhoneNo());
        retVal.setCreatedAt(new Date());
        return productRepository.save(retVal);
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
        var documents = documentService.saveAll(varietyDto.getDocuments());
        for (var document : documents){
            document.setDocIdentifier(variety.getId());
            document.setEntityName(variety.getClass().getSimpleName());
            documentService.save(document);
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
    public ProductDto update(ProductDto product) throws ServerException, InvalidInputException, IOException {
       var productEntity = ObjectMapperUtils.map(product, Product.class);
       productEntity = productRepository.save(productEntity);
       var varieties = new ArrayList<VarietyDto>();
       for (var dto : product.getVarieties()){
           var varietyEntity = ObjectMapperUtils.map(dto, Variety.class);
           varietyEntity = varietyRepository.save(varietyEntity);
           updateDocumentIfRequired(dto, varietyEntity);
           varieties.add(ObjectMapperUtils.map(varietyEntity, VarietyDto.class));
       }
       var retVal = ObjectMapperUtils.map(productEntity, ProductDto.class);
       retVal.setVarieties(varieties);
       return retVal;
    }

    private void updateDocumentIfRequired(VarietyDto varietyDto, Variety variety) throws ServerException {
        if (!CollectionUtils.isEmpty(varietyDto.getDocuments())){
            saveAndMapDocument(varietyDto, variety);
        }
    }

    /*

    @Transactional
    private List<Document> enforceDocumentLimitForProduct(List<String> documentId , VarietyDto variety) throws InvalidInputException {
        var boundedQueue = new BoundedQueue<String>(4);

        List<Document> documentList =
                documentService.fetchByDocIdentifierAndEntityName(
                        variety.getId(), variety.getClass().getSimpleName());
        for(Document document : documentList){
            boundedQueue.add(document.getId());
        }

        var documents = new ArrayList<Document>();
        for(String id : documentId){
            var document = documentService.fetchById(id);
            documents.add(document);
            String oldestDocId = boundedQueue.add(document.getId());
            if (oldestDocId != null && !oldestDocId.isEmpty()) {
                Document oldDocument = documentService.fetchById(oldestDocId);
                documentService.delete(oldDocument);
            }
        }
        mapDocument( , documents);
        return documentService.fetchByDocIdentifierAndEntityName(existProduct.getId(),
                existProduct.getClass().getSimpleName());
    }

     */

    private void mapDocument(Product product, List<Document> documents) {
        for(var document : documents) {
            document.setEntityName(product.getClass().getSimpleName());
            document.setDocIdentifier(product.getId());
            documentService.save(document);
        }
    }


    public String deleteProduct(List<String> productIds) throws  InvalidInputException {
        for (var productId : productIds){
            var product = productRepository.findById(productId);
            product.ifPresent(value -> productRepository.delete(product.get()));
        }
        return DELETE_MESSAGE;
    }

    public boolean isReduceQuantityValid(String code, Integer quantity) throws InvalidInputException {
        /*Product product = fetchProductByCode(code);
        assert quantity != null;
        return product.getQuantity()>=quantity;*/
        return true;
    }

    public String deleteWholeProduct(Product product) {
        productRepository.delete(product);
        return WHOLE_PRODUCT_DELETE_MESSAGE;
    }

    public Product fetchProductByCode(String code) throws InvalidInputException {
        return productRepository.findByCode(code)
                .orElseThrow(() -> new InvalidInputException("Product not found with code "+code));
    }

    public PageableResponse<ProductVarietyResponse> fetchProducts(final ProductSearchCriteria searchCriteria){
        var pageable = PageableUtils.createPageable(searchCriteria);
        Page<Variety> varieties = varietyRepository.findAll(
                VarietySpecifications.buildSearchCriteria(searchCriteria),
                pageable
        );
        var reslutList = varieties.getContent().stream()
                .map(this::getProductVarietyResponse)
                .filter(Objects::nonNull)
                .toList();
        return new PageableResponse<>(reslutList, searchCriteria);
    }

    /**
     * takes product entity as input and gives productDto. In case
     * of error return null.
     *
     * @param variety variety entity
     * @return null or Product Variety Response
     */
    private ProductVarietyResponse getProductVarietyResponse(Variety variety) {
        ProductVarietyResponse retVal = null;
        try{
            var product = fetchById(variety.getProduct().getId());
            var documents = documentService.fetchByDocIdentifierAndEntityName(variety.getId(), variety.getClass().getSimpleName());
            var documentIds = documents.stream()
                    .map(Document::getId)
                    .toList();
            retVal = ProductVarietyResponse.buildByProductVarietyAndDocuments()
                    .product(product)
                    .variety(variety)
                    .documents(documentIds)
                    .build();
        } catch (InvalidInputException ignored){}
        return retVal;
    }

    public void handleCancelOrder(Order order) throws JsonProcessingException, InvalidInputException {
        ObjectMapper mapper = new ObjectMapper();
        BoughtProductDetailsDto[] boughtProductList = mapper.readValue(order.getBoughtProductDetails(), BoughtProductDetailsDto[].class);
        for(var boughtProducts:boughtProductList) {
            var product = fetchProductByCode(boughtProducts.getCode());
//            Integer existQty = product.getQantity();
//            product.setQuantity(existQty+boughtProducts.getQuantity());
            productRepository.save(product);
        }
    }

    private Product fetchById(String id) throws InvalidInputException {
        var retVal = productRepository.findById(id);
        if (retVal.isPresent()){
            return retVal.get();
        }
        throw new InvalidInputException("Product not found with Id "+id);
    }
}

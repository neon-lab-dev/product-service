//package com.neonlab.product.service;
//import com.neonlab.common.annotations.Loggable;
//import com.neonlab.common.entities.Document;
//import com.neonlab.common.expectations.*;
//import com.neonlab.common.repositories.DocumentRepository;
//import com.neonlab.common.services.BoundedQueue;
//import com.neonlab.common.services.DocumentService;
//import com.neonlab.common.services.UserService;
//import com.neonlab.common.utilities.PageableUtils;
//import com.neonlab.product.dtos.ProductDto;
//import com.neonlab.product.entities.Product;
//import com.neonlab.product.models.ProductDeleteReq;
//import com.neonlab.product.models.responses.PageableResponse;
//import com.neonlab.product.models.searchCriteria.ProductSearchCriteria;
//import com.neonlab.product.repository.ProductRepository;
//import com.neonlab.common.utilities.ObjectMapperUtils;
//import com.neonlab.product.repository.specifications.ProductSpecifications;
//import jakarta.transaction.Transactional;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Comparator;
//import java.util.List;
//import java.util.Objects;
//import java.util.stream.Collectors;
//
//
//
//@Service
//@Loggable
//@Slf4j
//public class ProductService {
//
//    public final static String BRAND = "non-branded";
//
//    public final static String TAG = "Tags1";
//
//    public final static String MESSAGE = "Product unique code already exists or This Product Already Exists";
//
//    public final static String DELETE_MESSAGE = "Product Quantity Reduce Successfully";
//
//    public final static String WHOLE_PRODUCT_DELETE_MESSAGE = "Product Deleted Successfully";
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    @Autowired
//    //private DocumentService documentService;
//
//    @Autowired
//    private UserService userService;
//
//
//    public ProductDto addProduct(ProductDto productReqDto , List<MultipartFile> files) throws ServerException, InvalidInputException {
//        var documents =  documentService.saveAll(files);
//        var product = ObjectMapperUtils.map(productReqDto, Product.class);
//        productReqDto.setDocumentIds(
//                documents.stream()
//                        .map(Document::getId)
//                        .toList()
//                );
//        mapDocument(product , documents);
//        setDefaults(product);
//        product.setCreatedBy(userService.getLoggedInUser().getId());
//        product = productRepository.save(product);
//        return ObjectMapperUtils.map(product, ProductDto.class);
//    }
//
//    private void setDefaults(Product product) {
//        product.setBrand(BRAND);
//        product.setTags(TAG);
//    }
//
//    public boolean existingProduct(String code) {
//        return productRepository.findByCode(code).isPresent();
//    }
//
//    @Transactional
//    public ProductDto updateProduct(ProductDto product , List<MultipartFile> files) throws ServerException, InvalidInputException, IOException {
//        boolean flag = true;
//        Product existProducts = fetchProductByCode(product.getCode());
//        if(product.getName() != null){
//            flag = false;
//            existProducts.setName(product.getName());
//        }
//        if(product.getQuantity() != null){
//            flag = false;
//            existProducts.setQuantity(product.getQuantity());
//        }
//        if(product.getBrand() != null){
//            flag = false;
//            existProducts.setBrand(product.getBrand());
//        }
//        if(product.getCategory() != null){
//            flag = false;
//            existProducts.setCategory(product.getCategory());
//        }
//        if(product.getSubCategory() != null){
//            existProducts.setSubCategory(product.getSubCategory());
//            flag = false;
//        }
//        if(product.getDescription() != null){
//            flag = false;
//            existProducts.setDescription(product.getDescription());
//        }
//        if(product.getPrice() != null){
//            flag = false;
//            existProducts.setPrice(product.getPrice());
//        }
//        if(product.getDiscountPercent() != null){
//            flag = false;
//            existProducts.setDiscountPercent(product.getDiscountPercent());
//        }
//        if(product.getUnits() != null && product.getUnits().getUnit() != null){
//            flag = false;
//            existProducts.setUnits(product.getUnits());
//        }
//        if(product.getVariety() != null){
//            flag = false;
//            existProducts.setVariety(product.getVariety());
//        }
//        if(flag){
//            throw new InvalidInputException("Please add at-least one value to update");
//        }
//        productRepository.save(existProducts);
//        List<String> boundedDocumentId = new ArrayList<>();
//        if(files != null && !files.isEmpty()) {
//
//            var documentIds = new ArrayList<>(documentService.saveAll(files).stream()
//                    .map(Document::getId)
//                    .toList());
//            List<Document> documents = enforceDocumentLimitForProduct(documentIds,existProducts);
//            for(Document document : documents){
//                boundedDocumentId.add(document.getId());
//            }
//        }
//        ProductDto productDto = ObjectMapperUtils.map(existProducts , ProductDto.class);
//        productDto.setDocumentIds(boundedDocumentId);
//        return productDto;
//    }
//
//    @Transactional
//    private List<Document> enforceDocumentLimitForProduct(List<String> documentId , Product existProduct) throws InvalidInputException {
//        var boundedQueue = new BoundedQueue<String>(4);
//        List<Document> documentList =
//                documentService.fetchByDocIdentifierAndEntityName(
//                        existProduct.getId(), existProduct.getClass().getSimpleName());
//        for(Document document : documentList){
//            boundedQueue.add(document.getId());
//        }
//        var documents = new ArrayList<Document>();
//        for(String id : documentId){
//            var document = documentService.fetchById(id);
//            documents.add(document);
//            String oldestDocId = boundedQueue.add(document.getId());
//            if (oldestDocId != null && !oldestDocId.isEmpty()) {
//                Document oldDocument = documentService.fetchById(oldestDocId);
//                documentService.delete(oldDocument);
//            }
//        }
//        mapDocument(existProduct , documents);
//        return documentService.fetchByDocIdentifierAndEntityName(existProduct.getId(),
//                existProduct.getClass().getSimpleName());
//    }
//
//    private void mapDocument(Product product, List<Document> documents) {
//        for(var document : documents) {
//            document.setEntityName(product.getClass().getSimpleName());
//            document.setDocIdentifier(product.getId());
//            documentService.save(document);
//        }
//    }
//
//
//    public String deleteProductApi(ProductDeleteReq productDeleteReq) throws  InvalidInputException {
//        Product product = fetchProductByCode(productDeleteReq.getCode());
//        if(productDeleteReq.getDeleteProduct()){
//            return deleteWholeProduct(product);
//        }
//        Integer existsQuantity = product.getQuantity();
//        assert productDeleteReq.getQuantity() != null;
//        Integer currentQuantity = existsQuantity - productDeleteReq.getQuantity();
//        product.setQuantity(currentQuantity);
//        productRepository.save(product);
//        log.warn("Now Your Product Quantity is {}", currentQuantity);
//        return DELETE_MESSAGE;
//    }
//
//    public boolean isReduceQuantityValid(String code, Integer quantity) throws InvalidInputException {
//        Product product = fetchProductByCode(code);
//        assert quantity != null;
//        return product.getQuantity()>=quantity;
//    }
//
//    public String deleteWholeProduct(Product product) {
//        productRepository.delete(product);
//        return WHOLE_PRODUCT_DELETE_MESSAGE;
//    }
//
//    public Product fetchProductByCode(String code) throws InvalidInputException {
//        return productRepository.findByCode(code)
//                .orElseThrow(() -> new InvalidInputException("Product not found with code "+code));
//    }
//
//    public PageableResponse<ProductDto> fetchProducts(final ProductSearchCriteria searchCriteria){
//        var pageable = PageableUtils.createPageable(searchCriteria);
//        Page<Product> products = productRepository.findAll(
//                ProductSpecifications.buildSearchCriteria(searchCriteria),
//                pageable
//        );
//        var reslutList = products.getContent().stream()
//                .map(ProductService::getProductDto)
//                .filter(Objects::nonNull)
//                .toList();
//        return new PageableResponse<>(reslutList, searchCriteria);
//    }
//
//    /**
//     * takes product entity as input and gives productDto. In case
//     * of error return null.
//     *
//     * @param product product entity
//     * @return null or productDto
//     */
//    private static ProductDto getProductDto(Product product) {
//        ProductDto retVal = null;
//        try{
//            retVal = ObjectMapperUtils.map(product, ProductDto.class);
//        } catch (ServerException ignored){}
//        return retVal;
//    }
//
//}

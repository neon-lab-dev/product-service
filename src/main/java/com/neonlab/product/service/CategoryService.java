package com.neonlab.product.service;
import com.neonlab.common.annotations.Loggable;
import com.neonlab.common.entities.Document;
import com.neonlab.common.expectations.InvalidInputException;
import com.neonlab.common.expectations.ServerException;
import com.neonlab.common.services.DocumentService;
import com.neonlab.common.utilities.ObjectMapperUtils;
import com.neonlab.common.utilities.StringUtil;
import com.neonlab.product.dtos.*;
import com.neonlab.product.entities.Category;
import com.neonlab.product.enums.CategoryType;
import com.neonlab.product.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@Loggable
@Slf4j
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private DocumentService documentService;

    @Transactional(rollbackOn = {InvalidInputException.class, ServerException.class})
    public CategoryDto add(CategoryDto categoryDto) throws InvalidInputException, ServerException {
        var category = save(categoryDto);
        var subCategoryDtos = saveSubCategories(category, categoryDto.getSubCategoryDtoList());
        saveAndMapDocument(category, categoryDto);
        var retVal = ObjectMapperUtils.map(category, CategoryDto.class);
        retVal.setSubCategoryDtoList(subCategoryDtos);
        retVal.setDocumentUrl(getDocumentUrl(category));
        return retVal;
    }

    private void saveAndMapDocument(Category category, CategoryDto categoryDto) throws ServerException {
        maintainDocSize(category);
        if (categoryDto.getDocument() != null) {
            var doc = documentService.save(categoryDto.getDocument());
            doc.setDocIdentifier(String.valueOf(category.getId()));
            doc.setEntityName(category.getClass().getSimpleName());
            documentService.save(doc);
        }
    }

    private Category save(CategoryDto categoryDto) throws ServerException {
        var category = ObjectMapperUtils.map(categoryDto, Category.class);
        if (categoryDto.getType().equals(CategoryType.ROOT.getType())) {
            category.setParentCategory(null);
        }
        return categoryRepository.save(category);
    }

    private List<SubCategoryDto> saveSubCategories(Category parentCategory, List<SubCategoryDto> subCategoryDtos) throws ServerException, InvalidInputException {
        List<SubCategoryDto> savedSubCategoryDtos = new ArrayList<>();
        for (var subCategoryDto : subCategoryDtos) {
            var subCategory = saveSubCategory(subCategoryDto, parentCategory);
            var subSubCategoryDtos = saveSubCategory2s(subCategory, subCategoryDto.getSubCategory2DtoList());
            var savedSubCategoryDto = ObjectMapperUtils.map(subCategory, SubCategoryDto.class);
            saveAndMapDocument(subCategory, subCategoryDto);
            savedSubCategoryDto.setSubCategory2DtoList(subSubCategoryDtos);
            savedSubCategoryDto.setDocumentUrl(getDocumentUrl(subCategory));
            savedSubCategoryDtos.add(savedSubCategoryDto);
        }
        return savedSubCategoryDtos;
    }

    private void saveAndMapDocument(Category subCategory, SubCategoryDto subCategoryDto) throws ServerException {
        maintainDocSize(subCategory);
        if (subCategoryDto.getDocument() != null) {
            var doc = documentService.save(subCategoryDto.getDocument());
            doc.setDocIdentifier(String.valueOf(subCategory.getId()));
            doc.setEntityName(subCategory.getClass().getSimpleName());
            documentService.save(doc);
        }
    }

    private Category saveSubCategory(SubCategoryDto subCategoryDto, Category parentCategory) throws ServerException, InvalidInputException {
        if (subCategoryDto.getName() != null && !isCategoryNameSame(subCategoryDto.getName())) {
            var subCategory = ObjectMapperUtils.map(subCategoryDto, Category.class);
            subCategory.setParentCategory(parentCategory);
            return categoryRepository.save(subCategory);
        }
        throw new InvalidInputException("subCategory name must be unique "+subCategoryDto.getName());
    }

    private List<SubCategory2Dto> saveSubCategory2s(Category parentCategory, List<SubCategory2Dto> subCategory2Dtos) throws ServerException, InvalidInputException {
        List<SubCategory2Dto> savedSubCategory2Dtos = new ArrayList<>();
        if(!CollectionUtils.isEmpty(subCategory2Dtos)) {
            for (SubCategory2Dto subCategory2Dto : subCategory2Dtos) {
                var subCategory2 = saveSubCategory2(subCategory2Dto, parentCategory);
                var savedSubCategory2Dto = ObjectMapperUtils.map(subCategory2, SubCategory2Dto.class);
                saveAndMapDocument(subCategory2, subCategory2Dto);
                savedSubCategory2Dto.setDocumentUrl(getDocumentUrl(subCategory2));
                savedSubCategory2Dtos.add(savedSubCategory2Dto);
            }
        }
        return savedSubCategory2Dtos;
    }

    private void saveAndMapDocument(Category subCategory2, SubCategory2Dto subCategory2Dto) throws ServerException {
        maintainDocSize(subCategory2);
        if (subCategory2Dto.getDocument() != null) {
            var doc = documentService.save(subCategory2Dto.getDocument());
            doc.setDocIdentifier(String.valueOf(subCategory2.getId()));
            doc.setEntityName(subCategory2.getClass().getSimpleName());
            documentService.save(doc);
        }
    }

    private void maintainDocSize(Category subCategory2) throws ServerException {
        var existingDocs = documentService.fetchByDocIdentifierAndEntityName(
                String.valueOf(subCategory2.getId()), subCategory2.getClass().getSimpleName());
        for (var doc : existingDocs) {
            documentService.delete(doc);
        }
    }

    private Category saveSubCategory2(SubCategory2Dto subCategory2Dto, Category parentCategory) throws ServerException, InvalidInputException {
        if (subCategory2Dto.getName() != null && !isCategoryNameSame(subCategory2Dto.getName())) {
            var subCategory2 = ObjectMapperUtils.map(subCategory2Dto, Category.class);
            subCategory2.setParentCategory(parentCategory);
            return categoryRepository.save(subCategory2);
        }
        throw new InvalidInputException("SubCategory2Dto name must be unique "+subCategory2Dto.getName());
    }

    private String getDocumentUrl(Category category) {
        var categoryDoc = documentService.fetchByDocIdentifierAndEntityName(String.valueOf(category.getId()), category.getClass().getSimpleName());
        return categoryDoc.stream()
                .map(Document::getUrl)
                .findFirst()
                .orElse(null);
    }

    @Transactional(rollbackOn = {InvalidInputException.class, ServerException.class})
    public CategoryDto update(CategoryDto categoryDto) throws InvalidInputException, ServerException {
        var existingCategory = categoryRepository.findByName(categoryDto.getName())
                .orElseThrow(() -> new InvalidInputException("Category not found"));


        if (categoryDto.getName() != null) {
            existingCategory.setName(categoryDto.getName());
        }

        if (categoryDto.getDocument() != null) {
            saveAndMapDocument(existingCategory, categoryDto);
        }

        var updatedCategory = categoryRepository.save(existingCategory);
        var retVal = ObjectMapperUtils.map(updatedCategory, CategoryDto.class);

        if (!CollectionUtils.isEmpty(categoryDto.getSubCategoryDtoList())) {
            var subCategoryDtos = saveSubCategories(updatedCategory, categoryDto.getSubCategoryDtoList());
            retVal.setSubCategoryDtoList(subCategoryDtos);
        }

        retVal.setDocumentUrl(getDocumentUrl(updatedCategory));
        return retVal;
    }

    public boolean isCategoryNameSame(String name) {
        var categoryWithSameName = findByName(name);
        if (categoryWithSameName == null) return false;
        return categoryWithSameName.getName().equals(name);
    }

    private Category findByName(String name) {
        return categoryRepository.findByName(name)
                .orElse(null);
    }

    public List<CategoryDto> get(String name){
        if(StringUtil.isNullOrEmpty(name)){
            return getRootList();
        }
        else{
            return getCategoryByName(name);
        }
    }

    private List<CategoryDto> getRootList() {
        return categoryRepository.findByType(CategoryType.ROOT.getType()).stream()
                .map(category -> {
                    try {
                        return ObjectMapperUtils.map(category, CategoryDto.class);
                    } catch (ServerException e) {
                        return null;
                    }
                }).
                filter(Objects::nonNull)
                .toList();
    }

    private List<CategoryDto> getCategoryByName(String name){
        var categoryMayBe = categoryRepository.findByName(name);
        if (categoryMayBe.isPresent()){
            var category = categoryMayBe.get();
            return category.getSubCategories().stream()
                    .map(subCategory -> {
                        try {
                            return ObjectMapperUtils.map(subCategory, CategoryDto.class);
                        } catch (ServerException e) {
                            return null;
                        }
                    }).
                    filter(Objects::nonNull).
                    toList();
        } else {
            return new ArrayList<>();
        }
    }



    public List<CategoryDto> getAll() throws ServerException {
        List<Category> categoryListMayBe = categoryRepository.findAll();
        return categoryListMayBe.stream()
                .map(this::mapCategoryToCategoryDto)
                .collect(Collectors.toList());
    }


    private CategoryDto mapCategoryToCategoryDto(Category category) {
        try {
            var categoryDto = ObjectMapperUtils.
                    map(category,CategoryDto.class);
            categoryDto.setDocumentUrl(getDocumentUrl(category));
            if(category.getSubCategories() != null){
                List<SubCategoryDto> subCategoryDtos = category.getSubCategories()
                        .stream().map(this::mapCategoryToSubCategoryDto)
                        .toList();
                categoryDto.setSubCategoryDtoList(subCategoryDtos);
            }
            return categoryDto;
        }catch (ServerException e){
            return null;
        }
    }


    private SubCategoryDto mapCategoryToSubCategoryDto(Category subCategory) {
        try {
            var subCategoryDto = ObjectMapperUtils.map(subCategory, SubCategoryDto.class);
            subCategoryDto.setDocumentUrl(getDocumentUrl(subCategory));

            if (subCategory.getSubCategories() != null) {
                List<SubCategory2Dto> subCategory2Dtos = subCategory.getSubCategories()
                        .stream().map(this::mapCategoryToSubCategory2Dto)
                        .toList();
                subCategoryDto.setSubCategory2DtoList(subCategory2Dtos);
            }

            return subCategoryDto;
        }catch (ServerException e){
            return null;
        }
    }

    private SubCategory2Dto mapCategoryToSubCategory2Dto(Category subCategory2)  {
        try {
            var subCategory2Dto = ObjectMapperUtils.map(subCategory2, SubCategory2Dto.class);
            subCategory2Dto.setDocumentUrl(getDocumentUrl(subCategory2));
            return subCategory2Dto;
        }catch (ServerException e){
            return null;
        }
    }

    public boolean isExistingCategory(String category) {
        return categoryRepository.existsByName(category);
    }
}

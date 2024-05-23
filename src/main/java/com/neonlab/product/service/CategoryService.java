package com.neonlab.product.service;
import com.neonlab.common.annotations.Loggable;
import com.neonlab.common.entities.Document;
import com.neonlab.common.expectations.InvalidInputException;
import com.neonlab.common.expectations.ServerException;
import com.neonlab.common.services.DocumentService;
import com.neonlab.common.utilities.ObjectMapperUtils;
import com.neonlab.product.dtos.*;
import com.neonlab.product.entities.Category;
import com.neonlab.product.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Loggable
@Slf4j
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private DocumentService documentService;

    @Transactional
    public CategoryDto add(CategoryDto categoryDto) throws InvalidInputException, ServerException {
        var category = save(categoryDto);
        var subCategoryDtos = saveSubCategories(category, categoryDto.getSubCategoryDtoList());
        saveAndMapDocument(category,categoryDto);
        var retVal = ObjectMapperUtils.map(category, CategoryDto.class);
        retVal.setSubCategoryDtoList(subCategoryDtos);
        retVal.setDocumentUrl(getDocumentUrl(category));
        return retVal;
    }

    @Transactional
    private void saveAndMapDocument(Category category, CategoryDto categoryDto) throws ServerException {
        maintainDocSize(category);
        if(categoryDto.getDocument() != null){
            var doc = documentService.save(categoryDto.getDocument());
            doc.setDocIdentifier(String.valueOf(category.getId()));
            doc.setEntityName(category.getClass().getSimpleName());
            documentService.save(doc);
        }
    }

    @Transactional
    private Category save(CategoryDto categoryDto) throws ServerException {
        var category = ObjectMapperUtils.map(categoryDto, Category.class);
        if (categoryDto.getType().equals("root")) {
            category.setParentCategory(null);
        }
        return categoryRepository.save(category);
    }

    @Transactional
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

    @Transactional
    private void saveAndMapDocument(Category subCategory, SubCategoryDto subCategoryDto) throws ServerException {
        maintainDocSize(subCategory);
        if(subCategoryDto.getDocument() != null){
            var doc = documentService.save(subCategoryDto.getDocument());
            doc.setDocIdentifier(String.valueOf(subCategory.getId()));
            doc.setEntityName(subCategory.getClass().getSimpleName());
            documentService.save(doc);
        }
    }


    private Category saveSubCategory(SubCategoryDto subCategoryDto, Category parentCategory) throws ServerException, InvalidInputException {
        if(isCategoryNameSame(subCategoryDto.getName())) {
            var subCategory = ObjectMapperUtils.map(subCategoryDto, Category.class);
            subCategory.setParentCategory(parentCategory);
            return categoryRepository.save(subCategory);
        }
        throw new InvalidInputException("Category name must be unique");
    }

    @Transactional
    private List<SubCategory2Dto> saveSubCategory2s(Category parentCategory, List<SubCategory2Dto> subCategory2Dtos) throws ServerException, InvalidInputException {
        List<SubCategory2Dto> savedSubCategory2Dtos = new ArrayList<>();
        for (SubCategory2Dto subCategory2Dto : subCategory2Dtos) {
            var subCategory2 = saveSubCategory2(subCategory2Dto, parentCategory);
            var savedSubCategory2Dto = ObjectMapperUtils.map(subCategory2, SubCategory2Dto.class);
            saveAndMapDocument(subCategory2, subCategory2Dto);
            savedSubCategory2Dto.setDocumentUrl(getDocumentUrl(subCategory2));
            savedSubCategory2Dtos.add(savedSubCategory2Dto);
        }
        return savedSubCategory2Dtos;
    }

    @Transactional
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
        var subCategory2 = ObjectMapperUtils.map(subCategory2Dto, Category.class);
        subCategory2.setParentCategory(parentCategory);
        return categoryRepository.save(subCategory2);
    }
    
    private String getDocumentUrl(Category category) {
        var categoryDoc = documentService.fetchByDocIdentifierAndEntityName(String.valueOf(category.getId()), category.getClass().getSimpleName());
        return categoryDoc.stream()
                .map(Document::getUrl)
                .findFirst()
                .orElse(null);
    }

    @Transactional
    public CategoryDto update(CategoryDto categoryDto) throws InvalidInputException, ServerException {
        var existingCategory = findByName(categoryDto.getName());
        existingCategory.setName(categoryDto.getName());
        var updatedCategory = categoryRepository.save(existingCategory);
        var subCategoryDtos = saveSubCategories(updatedCategory, categoryDto.getSubCategoryDtoList());
        saveAndMapDocument(updatedCategory, categoryDto);
        var retVal = ObjectMapperUtils.map(updatedCategory, CategoryDto.class);
        retVal.setSubCategoryDtoList(subCategoryDtos);
        retVal.setDocumentUrl(getDocumentUrl(updatedCategory));
        return retVal;
    }

    public boolean isCategoryNameSame(String name) throws InvalidInputException {
        var categoryWithSameName = findByName(name);
        if(categoryWithSameName == null)return false;
        return categoryWithSameName.getName().equals(name);
    }

    private Category findByName(String name) {
        return categoryRepository.findByName(name).orElse(null);
    }
}
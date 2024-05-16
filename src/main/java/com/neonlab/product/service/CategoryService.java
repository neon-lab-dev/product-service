package com.neonlab.product.service;

import com.neonlab.common.annotations.Loggable;
import com.neonlab.common.expectations.InvalidInputException;
import com.neonlab.common.expectations.ServerException;
import com.neonlab.common.utilities.ObjectMapperUtils;
import com.neonlab.product.dtos.*;
import com.neonlab.product.entities.Category;
import com.neonlab.product.entities.Product;
import com.neonlab.product.entities.Variety;
import com.neonlab.product.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@Loggable
@Slf4j
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public CategoryDto add(CategoryDto categoryDto) throws InvalidInputException, ServerException {
        var category = save(categoryDto);
            var subCategories = saveAndMapSubCategories(category, categoryDto.getSubCategoryDtoList());
            var retVal = ObjectMapperUtils.map(category, CategoryDto.class);
            retVal.setSubCategoryDtoList(subCategories);
            return retVal;

    }
    private Category save(CategoryDto categoryDto) throws ServerException, InvalidInputException {
        var retVal = ObjectMapperUtils.map(categoryDto, Category.class);
        //setDefaultIfRequired(retVal);
        return categoryRepository.save(retVal);
    }

    private List<Category> saveAndMapSubCategories(Category category, List<SubCategoryDto> subCategoryDtos) throws ServerException, InvalidInputException {
        var retVal = category.getSubCategories();
        for (var subCategoryDto : subCategoryDtos){
            var category1 = saveSubCategory(subCategoryDto,category);
            saveAndMapSubCategories2(category1,subCategoryDto.getSubCategory2DtoList());
            retVal.add(ObjectMapperUtils.map(category1, Category.class));
        }
        return retVal;
    }
    private Category saveSubCategory(SubCategoryDto subCategoryDto, Category category) throws ServerException, InvalidInputException {
        var entity = ObjectMapperUtils.map(subCategoryDto, Category.class);
        entity.setCategory(category);
        return categoryRepository.save(entity);
    }

    private List<Category> saveAndMapSubCategories2(Category category, List<SubCategory2Dto> subCategory2Dtos) throws ServerException, InvalidInputException {
        var retVal = category.getSubCategories();
        for (var subCategoryDto : subCategory2Dtos){
            var category1 = saveSubCategory2(subCategoryDto,category);
            retVal.add(ObjectMapperUtils.map(category1, Category.class));
        }
        return retVal;
    }
    private Category saveSubCategory2(SubCategory2Dto subCategory2Dto, Category category) throws ServerException, InvalidInputException {
        var entity = ObjectMapperUtils.map(subCategory2Dto, Category.class);
        entity.setCategory(category);
        return categoryRepository.save(entity);
    }

//    private void setDefaultIfRequired(Product product){
//        if (Objects.nonNull(product.getBrand()) && product.getBrand().isEmpty()){
//            product.setBrand(BRAND);
//        }
//        if (Objects.nonNull(product.getTags()) && product.getTags().isEmpty()){
//            product.setTags(TAG);
//        }
//    }
}

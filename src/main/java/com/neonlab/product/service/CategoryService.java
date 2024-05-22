package com.neonlab.product.service;
import com.neonlab.common.annotations.Loggable;
import com.neonlab.common.expectations.InvalidInputException;
import com.neonlab.common.expectations.ServerException;
import com.neonlab.common.utilities.ObjectMapperUtils;
import com.neonlab.common.utilities.StringUtil;
import com.neonlab.product.dtos.*;
import com.neonlab.product.entities.Category;
import com.neonlab.product.enums.CategoryType;
import com.neonlab.product.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Loggable
@Slf4j
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public CategoryDto add(CategoryDto categoryDto) throws InvalidInputException, ServerException {
        var category = save(categoryDto);
        var subCategoryDtos = saveSubCategories(category, categoryDto.getSubCategoryDtoList());
        var retVal = ObjectMapperUtils.map(category, CategoryDto.class);
        retVal.setSubCategoryDtoList(subCategoryDtos);
        return retVal;
    }

    private Category save(CategoryDto categoryDto) throws ServerException {
        var category = ObjectMapperUtils.map(categoryDto, Category.class);
        if (categoryDto.getType().equals("root")) {
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
            savedSubCategoryDto.setSubCategory2DtoList(subSubCategoryDtos);
            savedSubCategoryDtos.add(savedSubCategoryDto);
        }
        return savedSubCategoryDtos;
    }

    private Category saveSubCategory(SubCategoryDto subCategoryDto, Category parentCategory) throws ServerException, InvalidInputException {
        var subCategory = ObjectMapperUtils.map(subCategoryDto, Category.class);
        subCategory.setParentCategory(parentCategory);
        return categoryRepository.save(subCategory);
    }

    private List<SubCategory2Dto> saveSubCategory2s(Category parentCategory, List<SubCategory2Dto> subCategory2Dtos) throws ServerException, InvalidInputException {
        List<SubCategory2Dto> savedSubCategory2Dtos = new ArrayList<>();
        for (SubCategory2Dto subCategory2Dto : subCategory2Dtos) {
            var subCategory2 = saveSubCategory2(subCategory2Dto, parentCategory);
            var savedSubCategory2Dto = ObjectMapperUtils.map(subCategory2, SubCategory2Dto.class);
            savedSubCategory2Dtos.add(savedSubCategory2Dto);
        }
        return savedSubCategory2Dtos;
    }

    private Category saveSubCategory2(SubCategory2Dto subCategory2Dto, Category parentCategory) throws ServerException, InvalidInputException {
        var subCategory2 = ObjectMapperUtils.map(subCategory2Dto, Category.class);
        subCategory2.setParentCategory(parentCategory);
        return categoryRepository.save(subCategory2);
    }

    public List<CategoryDto> get(String name) throws ServerException {
        List<CategoryDto> retVal = new ArrayList<>();
        if(StringUtil.isNullOrEmpty(name)){
            retVal = getRootList();
        }
        else{
            retVal.add(getCategoryByName(name));
        }
        return retVal;
    }

    private List<CategoryDto> getRootList() throws ServerException {
        List<Category> categoryList = categoryRepository.findAll();
        List<CategoryDto> retVal = new ArrayList<>();
        for(Category category:categoryList){
            if(category.getType() == CategoryType.ROOT){
                retVal.add(ObjectMapperUtils.map(category,CategoryDto.class));
            }
        }
        return retVal;
    }

    private CategoryDto getCategoryByName(String name) throws ServerException {
        Optional<Category> categoryOptional = categoryRepository.findByName(name);
        if(categoryOptional.isEmpty()){
            throw new ServerException("Category with given name does not exist.");
        }
        return ObjectMapperUtils.map(categoryOptional.get(),CategoryDto.class);
    }
}
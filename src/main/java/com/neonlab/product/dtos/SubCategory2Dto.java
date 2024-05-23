package com.neonlab.product.dtos;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.neonlab.product.annotations.UniqueCategoryName;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubCategory2Dto {
    @NotEmpty(message = "SubCategory2 name must not be empty")
    private String name;
    private String type="non_root";
    private MultipartFile document;
    private String documentUrl;
}

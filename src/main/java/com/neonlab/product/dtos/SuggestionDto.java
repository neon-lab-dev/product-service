package com.neonlab.product.dtos;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;


@Data
public class SuggestionDto {

    private String id;

    @NotEmpty(message = "Comment should not be empty")
    private String comment;
    private String createdAt;
    private String createdBy;
    private String modifiedAt;
    private String modifiedBy;
}

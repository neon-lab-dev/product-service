package com.neonlab.product.dtos;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;



@Data
public class SuggestionDto {

    String id;

    @NotEmpty(message = "Comment should not be empty")
    String comment;
}

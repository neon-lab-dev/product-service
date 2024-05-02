package com.neonlab.product.dtos;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.Date;


@Data
public class SuggestionDto {

    String id;

    @NotEmpty(message = "Comment should not be empty")
    String comment;
    Date createdAt;
    private String createdBy;
    private Date modifiedAt;
    private String modifiedBy;
}

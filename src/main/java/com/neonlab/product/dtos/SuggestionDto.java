package com.neonlab.product.dtos;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.Date;


@Data
public class SuggestionDto {

    private String id;

    @NotEmpty(message = "Comment should not be empty")
    private String comment;
    private Date createdAt;
    private String createdBy;
    private Date modifiedAt;
    private String modifiedBy;
}

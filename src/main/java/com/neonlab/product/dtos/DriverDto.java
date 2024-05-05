package com.neonlab.product.dtos;

import com.neonlab.common.validationGroups.UpdateValidationGroup;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DriverDto {

    @NotEmpty(groups = UpdateValidationGroup.class, message = "Driver id is mandatory.")
    private String id;
    @NotEmpty(message = "Driver name is mandatory.")
    private String name;
    @NotEmpty(message = "Driver contact no is mandatory.")
    private String contactNo;
    @NotEmpty(message = "Driver vehicle no is mandatory.")
    private String vehicleNo;
    private boolean available;

}

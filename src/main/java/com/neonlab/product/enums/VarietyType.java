package com.neonlab.product.enums;

import com.neonlab.common.expectations.InvalidInputException;
import lombok.Getter;

import java.util.List;
import java.util.Objects;

@Getter
public enum VarietyType {

    SIZE("size", List.of("S", "L","M","XL")),
    WEIGHT("weight", List.of("kg", "gms")),
    PACK_OF("pack of", List.of("pcs"));

    private final String value;
    private final List<String> supportedUnits;

    VarietyType(String value, List<String> supportedUnits){
        this.value = value;
        this.supportedUnits = supportedUnits;
    }

    public static VarietyType fromValue(String value) throws InvalidInputException {
        for (var type : VarietyType.values()){
            if(Objects.equals(type.getValue(), value)){
                return type;
            }
        }
        throw new InvalidInputException("Unknown variety: "+value);
    }
}

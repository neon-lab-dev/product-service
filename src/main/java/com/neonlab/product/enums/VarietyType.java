package com.neonlab.product.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.neonlab.common.expectations.InvalidInputException;
import lombok.Getter;

import java.util.List;
import java.util.Objects;

@Getter
public enum VarietyType {

    SIZE("size"),
    WEIGHT("weight"),
    PACK_OF("pack of"),
    PRICE("price"),
    LITER("liter");

    private final String value;

    VarietyType(String value){
        this.value = value;
    }

    public static VarietyType fromString(String value) throws InvalidInputException {
        for (var type : VarietyType.values()){
            if(Objects.equals(type.getValue(), value)){
                return type;
            }
        }
        throw new InvalidInputException("Unknown variety: "+value);
    }
}

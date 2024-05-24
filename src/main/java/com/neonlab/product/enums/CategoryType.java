package com.neonlab.product.enums;

import lombok.Getter;

@Getter
public enum CategoryType {

    ROOT("root"),
    NON_ROOT("non_root");

    private final String type;

    CategoryType(String type){
        this.type = type;
    }

}

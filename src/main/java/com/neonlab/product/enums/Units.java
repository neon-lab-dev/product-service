package com.neonlab.product.enums;
import lombok.Getter;

@Getter
public enum Units {
    KG("kg"),
    GRAM("Gram"),
    PCS("Pcs");
    public final String unit;
    Units(String unit) {
        this.unit = unit;
    }

}

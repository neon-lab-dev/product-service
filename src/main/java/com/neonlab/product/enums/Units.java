package com.neonlab.product.enums;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.neonlab.common.expectations.InvalidInputException;
import lombok.Getter;


@Getter
public enum Units {
    KG("kg"),
    GRAM("gm"),
    PCS("pcs"),
    S("small"),
    L("large"),
    XL("extra large"),
    M("medium"),
    RS("rs"),
    ML("ml");
    private final String unit;
    Units(String unit) {
        this.unit = unit;
    }

    @JsonCreator
    public static Units fromString(String inputUnit) throws InvalidInputException {
        for (Units unit : Units.values()) {
            if (unit.unit.equalsIgnoreCase(inputUnit) || unit.name().equalsIgnoreCase(inputUnit)) {
                return unit;
            }
        }
        throw new InvalidInputException("Unknown unit: " + inputUnit);
    }
}

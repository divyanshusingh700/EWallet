package com.truecodes.WalletServiceApplication.model;

import lombok.Getter;

@Getter
public enum CurrencyType {
    INR("INR"),
    LP_INR("LP-INR");

    private final String dbValue;

    CurrencyType(String dbValue) {
        this.dbValue = dbValue;
    }

    public static CurrencyType fromDbValue(String dbValue) {
        for (CurrencyType type : values()) {
            if (type.dbValue.equalsIgnoreCase(dbValue)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown currency type: " + dbValue);
    }
}

package com.truecodes.WalletServiceApplication.utils;

import com.truecodes.WalletServiceApplication.model.CurrencyType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CurrencyTypeConverter implements AttributeConverter<CurrencyType, String> {

    @Override
    public String convertToDatabaseColumn(CurrencyType attribute) {
        return attribute != null ? attribute.getDbValue() : null;
    }

    @Override
    public CurrencyType convertToEntityAttribute(String dbData) {
        return dbData != null ? CurrencyType.fromDbValue(dbData) : null;
    }
}


package com.truecodes.WalletServiceApplication.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalletDetailsRespDTO {
    private String name;
    private String walletNumber;
    private Double totalAmount;
    private CurrencyType currencyType;
    private String status;
}

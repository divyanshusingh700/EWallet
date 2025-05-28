package com.truecodes.WalletServiceApplication.model;

import com.truecodes.WalletServiceApplication.model.CurrencyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferResponseDTO {
    private String senderName;
    private String receiverName;
    private String senderWalletNumber;
    private String receiverWalletNumber;
    private Double amountTransferred;
    private CurrencyType currencyType;
    private String status;
    private String transactionTime;
    private String message;
}


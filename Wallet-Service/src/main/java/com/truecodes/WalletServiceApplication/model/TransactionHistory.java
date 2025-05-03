package com.truecodes.WalletServiceApplication.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "transaction_history")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, name = "sender_phone_number")
    private String senderContact;

    @Column(nullable = false, name = "sender_wallet_number")
    private String senderWalletNumber;

    @Column(nullable = false, name = "receiver_wallet_number")
    private String receiverWalletNumber;

    @Column(nullable = false, name = "receiver_phone_number")
    private String receiverContact;

    @Column(nullable = false)
    private String senderName;

    @Column(nullable = false)
    private String receiverName;

    @Column(nullable = false)
    private Double amountTransferred;

    @Column(nullable = false)
    private LocalDateTime transactionTime;

    @Column(nullable = false)
    private String status; // SUCCESS, FAILED, etc.

    private CurrencyType currencyType;
}

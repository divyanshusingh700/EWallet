package com.truecodes.WalletServiceApplication.service;

import com.truecodes.WalletServiceApplication.exceptionHandler.ClientSideAPIRequestException;
import com.truecodes.WalletServiceApplication.model.TransactionHistory;
import com.truecodes.WalletServiceApplication.model.Wallet;
import com.truecodes.WalletServiceApplication.repository.TxnHistoryRepository;
import com.truecodes.WalletServiceApplication.repository.WalletRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private TxnHistoryRepository transactionHistoryRepository;

    @Transactional
    public String transferAmount(String senderContact, String receiverContact, Double amount) {

        Wallet senderWallet = walletRepository.findByContact(senderContact);

        Wallet receiverWallet = walletRepository.findByContact(receiverContact);

        if (senderWallet.getBalance() < amount) {
            throw new ClientSideAPIRequestException("Insufficient balance to make transfer", HttpStatus.CONFLICT);
        }

        senderWallet.setBalance(senderWallet.getBalance() - amount);
        receiverWallet.setBalance(receiverWallet.getBalance() + amount);

        walletRepository.save(senderWallet);
        walletRepository.save(receiverWallet);

        TransactionHistory transactionHistory = TransactionHistory.builder()
                .senderContact(senderContact)
                .receiverContact(receiverContact)
                .amount(amount)
                .transactionTime(LocalDateTime.now())
                .status("SUCCESS")
                .build();

        transactionHistoryRepository.save(transactionHistory);

        return "Amount transferred successfully!";
    }
}

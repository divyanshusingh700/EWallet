package com.truecodes.WalletServiceApplication.service;

import com.truecodes.WalletServiceApplication.exceptionHandler.ClientSideAPIRequestException;
import com.truecodes.WalletServiceApplication.model.*;
import com.truecodes.WalletServiceApplication.repository.TxnHistoryRepository;
import com.truecodes.WalletServiceApplication.repository.WalletRepository;
import com.truecodes.utilities.dto.UserDTO;
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

    @Autowired
    private UserClientService userClientService;

    @Transactional
    public TransferResponseDTO transferAmount(String senderContact, String receiverContact, Double amount, CurrencyType currencyType) {

        Wallet senderWallet = walletRepository.findByContact(senderContact);

        Wallet receiverWallet = walletRepository.findByContact(receiverContact);
        UserDTO sender = userClientService.getUserNameById(senderWallet.getUserId());
        UserDTO receiver = userClientService.getUserNameById(receiverWallet.getUserId());

        if (senderWallet.getTotalAmount() < amount) {
            throw new ClientSideAPIRequestException("Insufficient balance to make transfer", HttpStatus.BAD_REQUEST);
        }

        senderWallet.setTotalAmount(senderWallet.getTotalAmount() - amount);
        receiverWallet.setTotalAmount(receiverWallet.getTotalAmount() + amount);

        walletRepository.save(senderWallet);
        walletRepository.save(receiverWallet);

        // need to move this to txn_initiated_topic kafka listener
        TransactionHistory transactionHistory = TransactionHistory.builder()
                .senderContact(senderContact)
                .receiverContact(receiverContact)
                .senderWalletNumber(senderWallet.getWalletSerial())
                .receiverWalletNumber(receiverWallet.getWalletSerial())
                .senderName(sender.getName())
                .receiverName(receiver.getName())
                .amountTransferred(amount)
                .transactionTime(LocalDateTime.now())
                .currencyType(currencyType)
                .status("PROCESSED")
                .build();

        transactionHistoryRepository.save(transactionHistory);

        return TransferResponseDTO.builder()
                .senderName(sender.getName())
                .receiverName(receiver.getName())
                .senderWalletNumber(senderWallet.getWalletSerial())
                .receiverWalletNumber(receiverWallet.getWalletSerial())
                .amountTransferred(amount)
                .currencyType(currencyType)
                .status("PROCESSED")
                .transactionTime(LocalDateTime.now().toString())
                .message("Amount transferred successfully!")
                .build();
    }
    public WalletDetailsRespDTO viewWalletDetails(String contact, CurrencyType currencyType) {
        Wallet wallet = walletRepository.findByContact(contact);
        UserDTO user = userClientService.getUserNameById(wallet.getUserId());
        String status = "";
        if(wallet.isActive()){
            status+="active";
        }
        return WalletDetailsRespDTO.builder()
                .totalAmount(wallet.getTotalAmount())
                .name(user.getName())
                .walletNumber(wallet.getWalletSerial())
                .currencyType(currencyType)
                .status(status)
                .build();
    }
}

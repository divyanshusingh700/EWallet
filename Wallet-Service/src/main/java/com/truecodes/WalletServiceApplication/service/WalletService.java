package com.truecodes.WalletServiceApplication.service;

import com.truecodes.WalletServiceApplication.exceptionHandler.ClientSideAPIRequestException;
import com.truecodes.WalletServiceApplication.model.*;
import com.truecodes.WalletServiceApplication.repository.TxnHistoryRepository;
import com.truecodes.WalletServiceApplication.repository.WalletRepository;
import com.truecodes.utilities.dto.UserDTO;
import jakarta.transaction.Transactional;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

@Service
public class WalletService implements UserDetailsService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private TxnHistoryRepository transactionHistoryRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private UserClientService userClientService;

    @Transactional
    public TransferResponseDTO transferAmount(String senderContact, String receiverContact, Double amount, String token, CurrencyType currencyType) {

        Wallet senderWallet = walletRepository.findByContact(senderContact);

        Wallet receiverWallet = walletRepository.findByContact(receiverContact);
        UserDTO sender = userClientService.getUserNameById(senderWallet.getUserId(), token);
        UserDTO receiver = userClientService.getUserNameById(receiverWallet.getUserId(), token);

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
    public WalletDetailsRespDTO viewWalletDetails(String contact, CurrencyType currencyType, String token) {
        Wallet wallet = walletRepository.findByContact(contact);
        UserDTO user = userClientService.getUserNameById(wallet.getUserId(),token);
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

    @Override
    public UserDetails loadUserByUsername(String userContact) throws UsernameNotFoundException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBasicAuth("txn-service", "txn-service");
        HttpEntity reqEntity = new HttpEntity(httpHeaders);

        JSONObject object = restTemplate.exchange("http://localhost:8081/user/userDetails?contact="+userContact,
                HttpMethod.GET, reqEntity, JSONObject.class).getBody();

        List<LinkedHashMap<String, String>> list = (List<LinkedHashMap<String, String>>)(object.get("authorities"));
        List<GrantedAuthority> reqAuthorities = list.stream().map(x-> x.get("authority")).map(x -> new SimpleGrantedAuthority(x)).collect(Collectors.toList());
        User user = new User((String) object.get("username"), (String) object.get("password"), reqAuthorities);
        System.out.println(object);
        return user;
    }
}

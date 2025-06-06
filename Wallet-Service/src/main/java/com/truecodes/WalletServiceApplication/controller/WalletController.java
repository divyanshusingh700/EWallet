package com.truecodes.WalletServiceApplication.controller;

import com.truecodes.WalletServiceApplication.dtos.TransferRequestDto;
import com.truecodes.WalletServiceApplication.model.CurrencyType;
import com.truecodes.WalletServiceApplication.model.TransferResponseDTO;
import com.truecodes.WalletServiceApplication.model.WalletDetailsRespDTO;
import com.truecodes.WalletServiceApplication.service.WalletService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    private static Logger logger = LoggerFactory.getLogger(WalletController.class);

    @PostMapping("/transfer")
    public ResponseEntity<TransferResponseDTO> transferAmount( @RequestHeader("Authorization") String authHeader, @Valid @RequestBody TransferRequestDto transferRequestDto) {
        String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;

        TransferResponseDTO result = walletService.transferAmount(transferRequestDto.getSenderContact(), transferRequestDto.getReceiverContact(), transferRequestDto.getAmount(), token, CurrencyType.INR);
        return ResponseEntity.ok(result);
    }
    @GetMapping("/view")
    public ResponseEntity<WalletDetailsRespDTO> viewWalletDetails(@RequestHeader("Authorization") String authHeader,@Valid @RequestBody Map<String, String> request) {
        logger.info("are we getting here");
        String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
        WalletDetailsRespDTO result = walletService.viewWalletDetails(request.get("contact"), CurrencyType.valueOf(request.get("currency")), token);
        return ResponseEntity.ok(result);
    }
}

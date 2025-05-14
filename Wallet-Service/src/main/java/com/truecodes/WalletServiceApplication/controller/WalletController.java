package com.truecodes.WalletServiceApplication.controller;

import com.truecodes.WalletServiceApplication.dtos.TransferRequestDto;
import com.truecodes.WalletServiceApplication.model.CurrencyType;
import com.truecodes.WalletServiceApplication.model.TransferResponseDTO;
import com.truecodes.WalletServiceApplication.model.WalletDetailsRespDTO;
import com.truecodes.WalletServiceApplication.service.WalletService;
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
    public ResponseEntity<TransferResponseDTO> transferAmount(@RequestBody TransferRequestDto transferRequestDto) {
        logger.info("are we getting here");
        TransferResponseDTO result = walletService.transferAmount(transferRequestDto.getSenderContact(), transferRequestDto.getReceiverContact(), transferRequestDto.getAmount(), CurrencyType.INR);
        return ResponseEntity.ok(result);
    }
    @GetMapping("/view")
    public ResponseEntity<WalletDetailsRespDTO> viewWalletDetails(@RequestBody Map<String, String> request) {
        logger.info("are we getting here");
        WalletDetailsRespDTO result = walletService.viewWalletDetails(request.get("contact"), CurrencyType.valueOf(request.get("currency")));
        return ResponseEntity.ok(result);
    }
}

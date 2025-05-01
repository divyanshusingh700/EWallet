package com.truecodes.WalletServiceApplication.controller;

import com.truecodes.WalletServiceApplication.dtos.TransferRequestDto;
import com.truecodes.WalletServiceApplication.service.WalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    private static Logger logger = LoggerFactory.getLogger(WalletController.class);

    @PostMapping("/transfer")
    public ResponseEntity<String> transferAmount(@RequestBody TransferRequestDto transferRequestDto) {
        logger.info("are we getting here");
//        try {
            String result = walletService.transferAmount(transferRequestDto.getSenderContact(), transferRequestDto.getReceiverContact(), transferRequestDto.getAmount());
            return ResponseEntity.ok(result);
//        } catch (CustomBadRequestException e) {
//            throw e;
//        }
    }
}

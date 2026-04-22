package com.truecodes.WalletServiceApplication.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
//@RequiredArgsConstructor
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequestDto {

    @NotBlank(message = "senderContact can not be blank")
    private String senderContact;
    @NotBlank(message = "receiverContact can not be blank")
    private String receiverContact;
    @NotNull(message = "Amount is required")
    private Double amount;
}

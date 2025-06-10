package com.truecodes.UserServiceApplication.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
//@RequiredArgsConstructor
@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "otp_entries")
public class OtpEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String otp;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime expiresAt;
    private int expiryTime;

    private boolean expired;

    private String clientIp;

    @Enumerated(EnumType.STRING)
    private ActionType actionType;

    private int attempts;

    public enum ActionType {
        REGISTRATION,
        LOGIN
    }

}


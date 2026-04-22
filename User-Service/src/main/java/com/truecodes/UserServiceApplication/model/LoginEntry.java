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
@Table(name = "login_entries")
public class LoginEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String otpCheckToken;
    private String refreshOtpCheckToken;
}

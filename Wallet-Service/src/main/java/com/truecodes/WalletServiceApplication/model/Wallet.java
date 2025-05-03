package com.truecodes.WalletServiceApplication.model;

import com.truecodes.UserServiceApplication.model.Users;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
//@RequiredArgsConstructor
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Wallet {

    private String userId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contact;

    private Double totalAmount;

    @CreationTimestamp
    private Date createdOn;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
//    private Users user;

    @UpdateTimestamp
    private Date updatedOn;

    @Column(name = "serial", nullable = false, unique = true, length = 12)
    private String walletSerial; //this should start with 0 and of length 12 digit

    @Column(name = "is_blocked", columnDefinition = "BOOLEAN DEFAULT true")
    private boolean isBlocked;

    @Column(name = "blocked_reason")
    private String blockedReason;

    @Column(name = "is_active", columnDefinition = "BOOLEAN DEFAULT true")
    private boolean active;

    @Column(nullable = false)
    private CurrencyType currency;

}

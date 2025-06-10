package com.truecodes.UserServiceApplication.repository;

import com.truecodes.UserServiceApplication.model.OtpEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpEntryRepository extends JpaRepository<OtpEntry, Long> {
    Optional<OtpEntry> findTopByEmailAndActionTypeOrderByCreatedAtDesc(String email, OtpEntry.ActionType actionType);
}

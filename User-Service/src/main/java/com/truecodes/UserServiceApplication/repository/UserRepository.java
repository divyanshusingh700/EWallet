package com.truecodes.UserServiceApplication.repository;

import com.truecodes.UserServiceApplication.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Integer> {
    Users findByContact(String contact);
    Optional<Users> findByUserId(String userId);

    boolean existsByContact(String contact);

    boolean existsByUserIdentifierValue(String userIdentifierValue);

    boolean existsByEmail(String email);
}

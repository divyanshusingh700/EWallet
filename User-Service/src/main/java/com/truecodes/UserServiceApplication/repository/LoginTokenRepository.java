package com.truecodes.UserServiceApplication.repository;

import com.truecodes.UserServiceApplication.model.LoginEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginTokenRepository extends JpaRepository<LoginEntry, Long> {

}

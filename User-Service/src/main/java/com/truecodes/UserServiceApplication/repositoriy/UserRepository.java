package com.truecodes.UserServiceApplication.repositoriy;

import com.truecodes.UserServiceApplication.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Integer> {
    Users findByContact(String contact);
}

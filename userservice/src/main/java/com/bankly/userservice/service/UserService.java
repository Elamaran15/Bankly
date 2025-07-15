package com.bankly.userservice.service;

import com.bankly.userservice.entity.Users;
import com.bankly.userservice.exception.LoginAuthException;
import com.bankly.userservice.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;


@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public Users registerUser(Users user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new LoginAuthException("Username already exists.", "BANK-AUTH-001");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new LoginAuthException("Email already registered.", "BANK-AUTH-002");
        }
        user.setPassword(user.getPassword());
        return userRepository.save(user);
    }

    public Optional<Users> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<Users> findById(UUID id) {
        return userRepository.findById(id);
    }





}

package com.bankly.userservice.controller;


import com.bankly.userservice.entity.Users;

import com.bankly.userservice.exception.LoginAuthException;
import com.bankly.userservice.service.AccountService;
import com.bankly.userservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class AuthController {

    private final UserService userService;
    private final AccountService accountService;


    public AuthController(UserService userService, AccountService accountService) {
        this.userService = userService;
        this.accountService = accountService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Users user) {
        try {
            Users registeredUser = userService.registerUser(user);
            accountService.createAccount(registeredUser); // Create a default account for the new user
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully and account created!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error registering user: " + e.getMessage());
        }
    }



    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Users loginRequest) {
        // In a no-security setup, login would simply check if the user exists
        // and if the password matches (after hashing). No JWT is generated.
        try {
            Users user = userService.findByUsername(loginRequest.getUsername())
                    .orElseThrow(() -> new LoginAuthException("Invalid username or password.","E001"));

            // For demo: simple password check (assuming passwordEncoder is still used in UserService)
            // In a real app, you'd use passwordEncoder.matches(rawPassword, encodedPassword)
            if (!user.getPassword().equals(loginRequest.getPassword())) { // This is NOT secure, just for demo without PasswordEncoder.matches
                // If you keep PasswordEncoder in UserService, you'd do:
                // if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                throw new LoginAuthException("Invalid username or password.","E001");
            }

            // No JWT generated, just a success message
            return ResponseEntity.ok("Login successful for user: " + loginRequest.getUsername());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during login: " + e.getMessage());
        }
    }


}

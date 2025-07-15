package com.bankly.transactionservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class Users {

    private UUID id;
    private String username;
    private String password; // Stored as hashed password
    private String email;
    private String firstName;
    private String lastName;



}

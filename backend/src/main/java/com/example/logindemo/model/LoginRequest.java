package com.example.logindemo.model;

import jakarta.annotation.Nonnull;
import lombok.Data;



@Data
public class LoginRequest {
    @Nonnull
    private String username;
    @Nonnull
    private String password;
}

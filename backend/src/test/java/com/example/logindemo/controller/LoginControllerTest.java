package com.example.logindemo.controller;

import com.example.logindemo.model.LoginRequest;
import com.example.logindemo.model.LoginResponse;
import com.example.logindemo.model.User;
import com.example.logindemo.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class LoginControllerTest {

    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private LoginController loginController;

    @BeforeEach
    public void setup() {
        userRepository = mock(UserRepository.class);
        bCryptPasswordEncoder = new BCryptPasswordEncoder();
        loginController = new LoginController(userRepository, bCryptPasswordEncoder);
    }

    @Test
    public void testLoginSuccess() {
        // Arrange
        String username = "user1";
        String password = "password1";
        String encodedPassword = bCryptPasswordEncoder.encode(password);
        User user = new User(1L, username, encodedPassword);
        LoginRequest loginRequest = new LoginRequest(username, password);
        when(userRepository.findByUsername(eq(username))).thenReturn(Optional.of(user));

        // Act
        ResponseEntity<?> responseEntity = loginController.login(loginRequest);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertTrue(responseEntity.getBody() instanceof LoginResponse);
        LoginResponse loginResponse = (LoginResponse) responseEntity.getBody();
        Assertions.assertEquals("Login successful", loginResponse.getMessage());
    }

    @Test
    public void testLoginFailure() {
        // Arrange
        String username = "user1";
        String password = "password1";
        LoginRequest loginRequest = new LoginRequest(username, password);
        when(userRepository.findByUsername(eq(username))).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> responseEntity = loginController.login(loginRequest);

        // Assert
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        Assertions.assertNull(responseEntity.getBody());
    }

    @Test
    public void testLoginInvalidPassword() {
        // Arrange
        String username = "user1";
        String password = "password1";
        String encodedPassword = bCryptPasswordEncoder.encode("password2");
        User user = new User(1L, username, encodedPassword);
        LoginRequest loginRequest = new LoginRequest(username, password);
        when(userRepository.findByUsername(eq(username))).thenReturn(Optional.of(user));

        // Act
        ResponseEntity<?> responseEntity = loginController.login(loginRequest);

        // Assert
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        Assertions.assertNull(responseEntity.getBody());
    }
}

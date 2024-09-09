package com.comsense.assignment.controller;

import com.comsense.assignment.dto.*;
import com.comsense.assignment.models.User;
import com.comsense.assignment.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSignInSuccess() {
        AuthRequest authRequest = new AuthRequest("username", "password" ,"email");
        JwtTokenDto jwtTokenDto = new JwtTokenDto("dasdsahj21", "Authentication successful", "1");
        ResponseEntity<Response> expectedResponse = ResponseEntity.ok(jwtTokenDto);

        when(authService.authenticate(any(AuthRequest.class))).thenReturn(expectedResponse);

        ResponseEntity<?> actualResponse = authController.signIn(authRequest);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void testSignInFailureInvalidCredentials() {
        AuthRequest authRequest = new AuthRequest("invalidUser", "wrongPassword","email");
        ErrorResponse errorResponse = ErrorResponse.builder()
                .error(List.of("error")).message("Invalid credentials")
                .status("401")
                .build();
        ResponseEntity<Response> expectedResponse = ResponseEntity.ok(errorResponse);
        when(authService.authenticate(any(AuthRequest.class))).thenReturn(expectedResponse);

        ResponseEntity<Response> actualResponse = authController.signIn(authRequest);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void testSignupSuccess() {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        ResponseEntity<Response> expectedResponse = ResponseEntity.ok(SuccessResponse.builder()
                .message("Success")
                .status("401")
                .build());

        when(authService.signup(any(RegisterRequest.class))).thenReturn(expectedResponse);

        ResponseEntity<?> actualResponse = authController.signup(registerRequest);

        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void testSignupFailure() {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        ResponseEntity<Response> expectedResponse = ResponseEntity.ok(ErrorResponse.builder()
                .error(List.of("error")).message("Invalid credentials")
                .status("401")
                .build());

        when(authService.signup(any(RegisterRequest.class))).thenReturn(expectedResponse);

        ResponseEntity<?> actualResponse = authController.signup(registerRequest);

        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(expectedResponse, actualResponse);
    }

}

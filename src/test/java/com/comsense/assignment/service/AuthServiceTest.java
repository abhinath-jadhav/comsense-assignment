package com.comsense.assignment.service;

import com.comsense.assignment.dto.AuthRequest;
import com.comsense.assignment.dto.ErrorResponse;
import com.comsense.assignment.dto.JwtTokenDto;
import com.comsense.assignment.dto.RegisterRequest;
import com.comsense.assignment.dto.Response;
import com.comsense.assignment.exceptions.JwtTokenMalformedException;
import com.comsense.assignment.exceptions.JwtTokenMissingException;
import com.comsense.assignment.models.User;
import com.comsense.assignment.models.UserRole;
import com.comsense.assignment.repository.RoleRepo;
import com.comsense.assignment.repository.UserRepo;
import com.comsense.assignment.utils.JwtTokenUtil;
import com.comsense.assignment.utils.builders.UserBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private UserRepo userRepo;

    @Mock
    private RoleRepo roleRepo;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAuthenticateSuccess() throws JwtTokenMalformedException, JwtTokenMissingException {

        AuthRequest authRequest = new AuthRequest("username", "password", "email@example.com");
        User user = new User(1L, "username", "john doe", "Password", "email@example.com", "contact", new HashSet<>(), "newToken", true);

        when(userRepo.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        doNothing().when(jwtTokenUtil).validateToken(anyString());
        when(jwtTokenUtil.generateToken(anyString(), anySet())).thenReturn("newToken");

        ResponseEntity<Response> response = authService.authenticate(authRequest);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("newToken", ((JwtTokenDto) response.getBody()).getToken());
    }

    @Test
    void testAuthenticateFailureInvalidCredentials() {
       
        AuthRequest authRequest = new AuthRequest("username", "invalidPassword", "email@example.com");

        when(userRepo.findByEmail(anyString())).thenReturn(Optional.empty());

        
        ResponseEntity<Response> response = authService.authenticate(authRequest);

        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Invalid credentials.", ((ErrorResponse) response.getBody()).getError().get(0));
    }

    @Test
    void testAuthenticateFailureTokenValidation() throws JwtTokenMalformedException, JwtTokenMissingException {
       
        AuthRequest authRequest = new AuthRequest("username", "password", "email@example.com");
        User user = new User(1L, "username", "john doe", "Password", "email@example.com", "contact", new HashSet<>(), "expiredToken", true);


        when(userRepo.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        doThrow(new JwtTokenMalformedException("Invalid Token")).when(jwtTokenUtil).validateToken(anyString());
        when(jwtTokenUtil.generateToken(anyString(), anySet())).thenReturn("newToken");

        
        ResponseEntity<Response> response = authService.authenticate(authRequest);

        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("newToken", ((JwtTokenDto) response.getBody()).getToken());
    }

    @Test
    void testSignupSuccess() {
       
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "password");
        UserRole userRole = new UserRole(1L, "USER", Set.of(new User()));

        when(userRepo.findByUsername(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(roleRepo.findById(anyLong())).thenReturn(Optional.of(userRole));
        when(userRepo.save(any(User.class))).thenReturn(new User());

        ResponseEntity<Response> response = authService.signup(registerRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testSignupFailureUserAlreadyExists() {
       
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "password");
        User user = new User(1L, "username", "john doe", "Password", "email@example.com", "contact", new HashSet<>(), "expiredToken", true);


        when(userRepo.findByUsername(anyString())).thenReturn(Optional.of(user));

        
        ResponseEntity<Response> response = authService.signup(registerRequest);

        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User already present with same email.", ((ErrorResponse) response.getBody()).getMessage());
    }
}

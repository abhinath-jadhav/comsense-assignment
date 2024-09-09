package com.comsense.assignment.service;

import com.comsense.assignment.dto.*;
import com.comsense.assignment.models.User;
import com.comsense.assignment.models.UserRole;
import com.comsense.assignment.repository.RoleRepo;
import com.comsense.assignment.repository.UserRepo;
import com.comsense.assignment.utils.JwtTokenUtil;
import com.comsense.assignment.utils.builders.UserBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AuthService {


    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public ResponseEntity<Response> authenticate(AuthRequest authRequest) {
        User user = null;
        try {
            user = authenticate(authRequest.getUsername(), authRequest.getPassword(), authRequest.getEmail());
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ErrorResponse.builder()
                            .status("401")
                            .error(List.of(e.getMessage()))
                            .message("Failed")
                            .build());
        }
        // generating token
        try {
            jwtTokenUtil.validateToken(user.getToken());
            return ResponseEntity.ok(new JwtTokenDto(user.getToken(),"200", user.getId().toString()));
        } catch (Exception e) {
            log.info("Token expired. Generating new token.");
        }
        HashSet<String> objects = user.getRoles().stream().map(UserRole::getName).collect(Collectors.toCollection(HashSet::new));
        String token = jwtTokenUtil.generateToken(authRequest.getEmail(), objects);
        user.setToken(token);
        userRepo.save(user);
        return ResponseEntity.ok(new JwtTokenDto(token,"200",user.getId().toString()));
    }

    private User authenticate(String username, String password, String email) {
        Optional<User> user;

        if (!StringUtils.isBlank(email)) {
            user = userRepo.findByEmail(email.trim().toLowerCase());
        } else if (!StringUtils.isBlank(username)) {
            user = userRepo.findByUsername(username.trim().toLowerCase());
        } else {
            throw new RuntimeException("Username or email is mandatory.");
        }
        if (user.isEmpty()) {
            throw new RuntimeException("Invalid credentials.");
        }
        if (!passwordEncoder.matches(password, user.get().getPassword())) {
            throw new RuntimeException("Invalid credentials.");
        }
        return user.get();
    }

    public ResponseEntity<Response> signup(RegisterRequest request) {
        Optional<User> userOptional = userRepo.findByUsername(request.getEmail());
        if (userOptional.isPresent()) {
            return ResponseEntity
                    .ok(
                            ErrorResponse.builder()
                                    .error(List.of("User already present with same email."))
                                    .message("User already present with same email.")
                                    .status("400")
                                    .build()
                    );
        }
        if (validatePassword(request)) {
            String encode = passwordEncoder.encode(request.getPassword());
            UserRole role = roleRepo.findById(1L).get();
            Set<UserRole> roles= new HashSet<>();
            roles.add(role);
            User user = UserBuilder.builder()
                    .password(encode)
                    .email(request.getEmail().toLowerCase())
                    .username(request.getEmail().toLowerCase())
                    .roles(roles)
                    .accountNonExpired(true)
                    .build();
            userRepo.save(user);
            AuthRequest authRequest = AuthRequest.builder()
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .password(request.getPassword())
                    .build();
            return authenticate(authRequest);
        }
        return ResponseEntity
                .ok(
                        ErrorResponse.builder()
                                .error(List.of("Password and confirm Password should match"))
                                .message("failed")
                                .status("400")
                                .build()
                );

    }

    public boolean validatePassword(RegisterRequest validateUserInfo) {
        return validateUserInfo.getPassword().equals(validateUserInfo.getConfirmPassword());
    }

}

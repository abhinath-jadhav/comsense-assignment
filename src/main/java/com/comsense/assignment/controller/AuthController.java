package com.comsense.assignment.controller;


import com.comsense.assignment.dto.AuthRequest;
import com.comsense.assignment.dto.RegisterRequest;
import com.comsense.assignment.dto.Response;
import com.comsense.assignment.models.User;
import com.comsense.assignment.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;


    @PostMapping("/sign-in")
    public ResponseEntity<Response> signIn(@RequestBody AuthRequest authRequest){
        return authService.authenticate(authRequest);
    }

    @PostMapping("/register")
    public ResponseEntity<Response> signup(@RequestBody RegisterRequest user){
        return authService.signup(user);
    }


}

package com.comsense.assignment.dto;

import lombok.Data;

@Data
public class ValidateUserInfo {
    private String username;
    private String password;
    private String confirmPassword;
}

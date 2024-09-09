package com.comsense.assignment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class RegisterRequest extends Response{

    private String email;
    private String password;
    private String confirmPassword;

}

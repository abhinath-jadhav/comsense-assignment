package com.comsense.assignment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {
    private String username;
    private String password;
    private String email;

    public static AuthRequestBuilder builder(){
        return new AuthRequestBuilder();
    }

    public static class AuthRequestBuilder {

        private String username;
        private String password;
        private String email;

        public AuthRequestBuilder username(String username) {
            this.username = username;
            return this;
        }

        public AuthRequestBuilder password(String password) {
            this.password = password;
            return this;
        }

        public AuthRequestBuilder email(String email) {
            this.email = email;
            return this;
        }
        public AuthRequest build(){
            return new AuthRequest(this.username, this.password, this.email);
        }
    }
}
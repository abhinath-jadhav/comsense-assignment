package com.comsense.assignment.utils.builders;

import com.comsense.assignment.models.User;
import com.comsense.assignment.models.UserRole;

import java.util.HashSet;
import java.util.Set;

public class UserBuilder {
    private Long id;
    private String username;
    private String name;
    private String password;
    private String email;
    private String contact;
    private Set<UserRole> roles = new HashSet<>();
    private String token;
    private boolean accountNonExpired;

    public UserBuilder username(String username) {
        this.username = username;
        return this;
    }

    public UserBuilder name(String name) {
        this.name = name;
        return this;
    }

    public UserBuilder password(String password) {
        this.password = password;
        return this;
    }

    public UserBuilder email(String email) {
        this.email = email;
        return this;
    }

    public UserBuilder contact(String contact) {
        this.contact = contact;
        return this;
    }

    public UserBuilder roles(Set<UserRole> roles) {
        this.roles = roles;
        return this;
    }

    public UserBuilder token(String token) {
        this.token = token;
        return this;
    }

    public UserBuilder accountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
        return this;
    }

    public User build() {
        User user = new User();
        user.setId(this.id);
        user.setUsername(this.username);
        user.setName(this.name);
        user.setPassword(this.password);
        user.setEmail(this.email);
        user.setContact(this.contact);
        user.setRoles(this.roles);
        user.setToken(this.token);
        user.setAccountNonExpired(this.accountNonExpired);
        return user;
    }

    public static UserBuilder builder(){
        return new UserBuilder();
    }
}

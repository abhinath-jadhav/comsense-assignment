package com.comsense.assignment.service;

import com.comsense.assignment.repository.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

}

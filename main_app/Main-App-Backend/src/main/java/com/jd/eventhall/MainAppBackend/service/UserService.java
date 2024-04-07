package com.jd.eventhall.MainAppBackend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jd.eventhall.MainAppBackend.model.User;
import com.jd.eventhall.MainAppBackend.repository.UserRepo;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    public Optional<User> getUserByUsername(String username) {
        return userRepo.getUserByUsername(username);
    }

    public Optional<User> getUserById(String id) {
        return userRepo.getUserById(id);
    }
}

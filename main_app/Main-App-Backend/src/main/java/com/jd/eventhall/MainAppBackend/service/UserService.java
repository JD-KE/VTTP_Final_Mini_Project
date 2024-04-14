package com.jd.eventhall.MainAppBackend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jd.eventhall.MainAppBackend.model.User;
import com.jd.eventhall.MainAppBackend.repository.TokenRepo;
import com.jd.eventhall.MainAppBackend.repository.UserRepo;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private TokenRepo tokenRepo;

    @Autowired
    private MeterRegistry meterRegistry;

    @PostConstruct
    public void buildActiveUserGauge() {
        Gauge.builder("users_logged_in", tokenRepo::countValidTokens)
        .description("Active users based on jwt. Custom metric")
        .baseUnit("Users")
        .register(meterRegistry);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepo.getUserByUsername(username);
    }

    public Optional<User> getUserById(String id) {
        return userRepo.getUserById(id);
    }

    public boolean usernameExists(String username) {
        return userRepo.usernameExists(username);
    }

    public boolean emailExists(String email) {
        return userRepo.emailExists(email);
    }
}

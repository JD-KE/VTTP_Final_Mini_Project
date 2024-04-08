package com.jd.eventhall.MainAppBackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jd.eventhall.MainAppBackend.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userSvc;

    @PostMapping("/check/username")
    public ResponseEntity<Boolean> usernameExists(@RequestBody String usernameString) {
        return ResponseEntity.ok().body(userSvc.usernameExists(usernameString));
    }

    @PostMapping("/check/email")
    public ResponseEntity<Boolean> emailExists(@RequestBody String emailString) {
        return ResponseEntity.ok().body(userSvc.emailExists(emailString));
    }
}

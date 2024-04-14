package com.jd.eventhall.MainAppBackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jd.eventhall.MainAppBackend.model.AuthenticationRequest;
import com.jd.eventhall.MainAppBackend.model.AuthenticationResponse;
import com.jd.eventhall.MainAppBackend.model.RegisterRequest;
import com.jd.eventhall.MainAppBackend.service.AuthenticationService;

import io.micrometer.core.instrument.MeterRegistry;
import jakarta.json.Json;
import jakarta.json.JsonObject;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private MeterRegistry meterRegistry;

    @Autowired
    private AuthenticationService authSvc;

    @PostMapping("/register")
    public ResponseEntity<String> register(
        @RequestBody RegisterRequest request
    ) {
        JsonObject jsonObject;
        if (request.getUsername().isEmpty() || request.getEmail().isEmpty() ||
        request.getPassword().isEmpty()) {
            jsonObject = Json.createObjectBuilder()
                .add("message", "Missing fields")
                .build();
            return ResponseEntity.badRequest().body(jsonObject.toString());
        }

        
        try {
            AuthenticationResponse response = authSvc.register(request).orElseThrow();
            jsonObject =  Json.createObjectBuilder()
                .add("access_token", response.getAccessToken())
                .add("refresh_token", response.getRefreshToken())
                .build();
        
            return ResponseEntity.ok(jsonObject.toString());
        } catch (Exception e) {
            jsonObject = Json.createObjectBuilder()
                .add("message", "User already exists")
                .build();
            return ResponseEntity.badRequest().body(jsonObject.toString());
        }

        
    }
    
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
        @RequestBody AuthenticationRequest request
    ) {
        meterRegistry.counter("login_counter").increment();
        return ResponseEntity.ok(authSvc.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<String> refreshToken(
    @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        JsonObject respObj = authSvc.refreshToken(authHeader);
        if(respObj.containsKey("message")){
            return ResponseEntity.status(respObj.getInt("status")).body(respObj.toString());
        }
        meterRegistry.counter("refresh_token_counter").increment();
        return ResponseEntity.ok().body(respObj.toString());
    }
}

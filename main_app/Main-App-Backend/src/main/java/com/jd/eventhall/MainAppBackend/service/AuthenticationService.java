package com.jd.eventhall.MainAppBackend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jd.eventhall.MainAppBackend.Utils.GenerateId;
import com.jd.eventhall.MainAppBackend.config.JwtService;
import com.jd.eventhall.MainAppBackend.model.AuthenticationRequest;
import com.jd.eventhall.MainAppBackend.model.AuthenticationResponse;
import com.jd.eventhall.MainAppBackend.model.RegisterRequest;
import com.jd.eventhall.MainAppBackend.model.Token;
import com.jd.eventhall.MainAppBackend.model.TokenType;
import com.jd.eventhall.MainAppBackend.model.User;
import com.jd.eventhall.MainAppBackend.repository.TokenRepo;
import com.jd.eventhall.MainAppBackend.repository.UserRepo;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private TokenRepo tokenRepo;

    @Autowired
    private JwtService jwtSvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    public Optional<AuthenticationResponse> register(RegisterRequest request) {

        if (userRepo.getUserByUsername(request.getUsername()).isPresent() || 
            userRepo.getUserByEmail(request.getEmail()).isPresent()) {
            return Optional.empty();
        }


        String id = GenerateId.generateId();
        while (userRepo.idExists(id)) {
            id = GenerateId.generateId();
        }
        var user = User.builder()
            .id(id)
            .username(request.getUsername())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(request.getRole())
            .build();
        var savedUser = userRepo.register(user);
        var jwtToken = jwtSvc.generateToken(user);
        var refreshToken = jwtSvc.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);
        return Optional.of(AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .build()
        );
    }
    

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
            )
        );
        
        User user = userRepo.getUserByUsername(request.getUsername())
            .orElseThrow();
        String jwtToken = jwtSvc.generateToken(user);
        String refreshToken = jwtSvc.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .build();
    }

    private void saveUserToken(User user, String jwtToken) {

        String id = GenerateId.generateId();
        while (tokenRepo.idExists(id)) {
            id = GenerateId.generateId();
        }
        var token = Token.builder()
            .id(id)
            .token(jwtToken)
            .tokenType(TokenType.BEARER)
            .expired(false)
            .revoked(false)
            .userId(user.getId())
            .build();
        tokenRepo.addToken(token);
    }

    private void revokeAllUserTokens(User user) {
        tokenRepo.revokeAllUserTokens(user.getId());
    }

    public JsonObject refreshToken(String authHeader) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        final String refreshToken;
        final String username;
        final User user;

        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            
            return builder.add("message", "Authorization not valid")
                .add("status", 400)
                .build();
        }
        refreshToken = authHeader.substring(7);
        username = jwtSvc.extractUsername(refreshToken);

        if (username != null) {
            Optional<User> userOpt = this.userRepo.getUserByUsername(username);
            if (userOpt.isEmpty()) {
                return builder.add("message", "User not found")
                .add("status", 403)
                .build();
            }
            user = userOpt.get();
                
            if (jwtSvc.isTokenValid(refreshToken, user)) {
                var accessToken = jwtSvc.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);

                return builder
                    .add("access_token", accessToken)
                    .add("refresh_token", refreshToken)
                    .build();
            } else {
                return builder.add("message", "Refresh Token Invalid")
                .add("status", 403)
                .build();
                
            }
            
        } else {
            return builder.add("message", "No User found")
                .add("status", 400)
                .build();
        }
    }
    
}

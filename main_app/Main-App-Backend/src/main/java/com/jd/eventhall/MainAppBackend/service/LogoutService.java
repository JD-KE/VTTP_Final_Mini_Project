package com.jd.eventhall.MainAppBackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import com.jd.eventhall.MainAppBackend.model.Token;
import com.jd.eventhall.MainAppBackend.repository.TokenRepo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class LogoutService implements LogoutHandler {

    @Autowired
    private TokenRepo tokenRepo;

    @Override
    public void logout(HttpServletRequest request,
    HttpServletResponse response, 
    Authentication authentication) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;

        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        jwt = authHeader.substring(7);
        Token storedToken = tokenRepo.getTokenByTokenString(jwt)
            .orElse(null);
        if (storedToken != null) {
            tokenRepo.revokeTokenByTokenString(jwt);
        
            SecurityContextHolder.clearContext();
        }
    }
    
}

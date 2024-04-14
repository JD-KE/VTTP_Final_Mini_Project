package com.jd.eventhall.MainAppBackend;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.jd.eventhall.MainAppBackend.config.JwtService;
import com.jd.eventhall.MainAppBackend.model.Token;
import com.jd.eventhall.MainAppBackend.repository.TokenRepo;

import io.jsonwebtoken.Claims;

@Component
public class ScheduledTasks {

    @Autowired
    private TokenRepo tokenRepo;

    @Autowired
    private JwtService jwtSvc;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    @Scheduled(cron = "0 0 * * * * ", zone = "UTC")
    public void deleteExpiredTokens() {
        System.out.println("deleting expired tokens");
        List<Token> tokens = tokenRepo.getAllTokens();

        for (Token token:tokens) {
            Date expiredDate = jwtSvc.extractClaim(token.getToken(), Claims::getExpiration);
            if(token.isExpired()) {

                if (new Date().getTime() - expiredDate.getTime() > jwtExpiration) {
                    tokenRepo.deleteTokenById(token.getId());
                }
            } else {
                if(new Date().getTime() - expiredDate.getTime()> 0l) {
                   tokenRepo.revokeTokenByTokenString(token.token);
                }
            }
        }
    }

}

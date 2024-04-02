package com.jd.eventhall.MainAppBackend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Token {
    
    public String id;
    public String token;
    public TokenType tokenType;
    public boolean revoked;
    public boolean expired;
    public String userId;
}

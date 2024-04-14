package com.jd.eventhall.MainAppBackend.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.jd.eventhall.MainAppBackend.model.Token;
import com.jd.eventhall.MainAppBackend.model.TokenType;

@Repository
public class TokenRepo {
    
    public static final String SQL_INSERT_TOKEN = """
        insert into tokens(id,token,token_type,revoked,expired,user_id) values (?,?,?,?,?,?)
        """;

    public static final String SQL_SELECT_TOKEN_BY_USERID = "select * from tokens where user_id like ?";

    public static final String SQL_SELECT_TOKEN_BY_ID = "select * from tokens where id like ?";

    public static final String SQL_SELECT_TOKEN_BY_TOKEN = "select * from tokens where token = ?";

    public static final String SQL_SELECT_All_TOKENS = """
            select * from tokens
            """;

    public static final String SQL_COUNT_VALID_TOKENS = """
            select count(*) from tokens where revoked = false or expired = false
            """;

    public static final String SQL_REVOKE_TOKENS_BY_USERID = """
            update tokens set
            revoked = true,
            expired = true
            where user_id = ?;
            """;

    public static final String SQL_REVOKE_TOKENS_BY_TOKEN = """
        update tokens set
        revoked = true,
        expired = true
        where token = ?;
        """;

    public static final String SQL_DELETE_TOKEN_BY_ID = """
            delete from tokens where id = ?
            """;

    @Autowired
    private JdbcTemplate template;

    public Optional<Token> getTokenByTokenString(String tokenString) {
        SqlRowSet rs = template.queryForRowSet(SQL_SELECT_TOKEN_BY_TOKEN, tokenString);

        if (!rs.next()) {
            return Optional.empty();
        }

        Token token = new Token(
            rs.getString("id"),
            rs.getString("token"),
            TokenType.BEARER,
            rs.getBoolean("revoked"),
            rs.getBoolean("expired"),
            rs.getString("user_id")
        );

        return Optional.of(token);
    }

    public List<Token> getAllTokens() {
        SqlRowSet rs = template.queryForRowSet(SQL_SELECT_All_TOKENS);
        List<Token> tokens = new ArrayList<>();

        while(rs.next()) {
            Token token = new Token(
                rs.getString("id"),
                rs.getString("token"),
                TokenType.BEARER,
                rs.getBoolean("revoked"),
                rs.getBoolean("expired"),
                rs.getString("user_id")
            );

            tokens.add(token);
        }

        return tokens;
    }

    public boolean addToken(Token token) {
         int rowsUpdated = template.update(SQL_INSERT_TOKEN, 
            token.getId(),
            token.getToken(),
            TokenType.BEARER.toString(),
            token.isRevoked(),
            token.isExpired(),
            token.getUserId());

        return rowsUpdated > 0;
    }

    public boolean revokeAllUserTokens(String id) {
        int rowsUpdated = template.update(SQL_REVOKE_TOKENS_BY_USERID, id);

        return rowsUpdated > 0;
    }

    public boolean revokeTokenByTokenString(String tokenString) {
        int rowsUpdated = template.update(SQL_REVOKE_TOKENS_BY_TOKEN, tokenString);

        return rowsUpdated > 0;
    }

    public boolean idExists(String id) {
        SqlRowSet rs = template.queryForRowSet(SQL_SELECT_TOKEN_BY_ID, id);
        
		return rs.next();
    }

    public boolean deleteTokenById(String id) {
        int rowsUpdated = template.update(SQL_DELETE_TOKEN_BY_ID, id);

        return rowsUpdated > 0;
    }

    public int countValidTokens() {
        SqlRowSet rs = template.queryForRowSet(SQL_COUNT_VALID_TOKENS);
        rs.next();
        return rs.getInt(1);
    }
}

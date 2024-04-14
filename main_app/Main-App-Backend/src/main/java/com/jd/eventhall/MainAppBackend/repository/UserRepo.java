package com.jd.eventhall.MainAppBackend.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.jd.eventhall.MainAppBackend.model.Role;
import com.jd.eventhall.MainAppBackend.model.User;


@Repository
public class UserRepo {

    public static final String SQL_SELECT_USER_BY_EMAIL = "select * from users where email like ?";

    public static final String SQL_SELECT_USER_BY_USERNAME = "select * from users where username like ?";

    public static final String SQL_SELECT_USER_BY_ID = "select * from users where id like ?";

    public static final String SQL_INSERT_USER = """
        insert into users(id,username,email,password,role) values (?,?,?,?,?)
        """;
    
    public static final String SQL_COUNT_EVENTS_PER_USER ="""
        select users.id, count(user_created) as events_count
            from users
            join events
            on events.user_created = users.id
            group by users.id;
            """;
    

    @Autowired
    private JdbcTemplate template;

    public Optional<User> getUserByEmail(String email) {
        
        SqlRowSet rs = template.queryForRowSet(SQL_SELECT_USER_BY_EMAIL, email);
        
		if (!rs.next()) {
            return Optional.empty();
        }

        User user = new User(rs.getString("id"),
            rs.getString("username"),
            rs.getString("email"),
            rs.getString("password"),
            Role.USER
        );
        
        return Optional.of(user);
    }

    public Optional<User> getUserByUsername(String username) {
        
        SqlRowSet rs = template.queryForRowSet(SQL_SELECT_USER_BY_USERNAME, username);
        
		if (!rs.next()) {
            return Optional.empty();
        }

        User user = new User(rs.getString("id"),
            rs.getString("username"),
            rs.getString("email"),
            rs.getString("password"),
            Role.USER
        );
        
        return Optional.of(user);
    }

    public Optional<User> getUserById(String id) {
        
        SqlRowSet rs = template.queryForRowSet(SQL_SELECT_USER_BY_ID, id);
        
		if (!rs.next()) {
            return Optional.empty();
        }

        User user = new User(rs.getString("id"),
            rs.getString("username"),
            rs.getString("email"),
            rs.getString("password"),
            Role.USER
        );
        
        return Optional.of(user);
    }

    public User register(User user) {
        int rowsUpdated = template.update(SQL_INSERT_USER, 
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getPassword(),
            Role.USER.toString());

        return user;
    }

    public boolean idExists(String id) {
        SqlRowSet rs = template.queryForRowSet(SQL_SELECT_USER_BY_ID, id);
        
		return rs.next();
    }

    public boolean usernameExists(String username) {
        SqlRowSet rs = template.queryForRowSet(SQL_SELECT_USER_BY_USERNAME, username);
        
		return rs.next();
    }

    public boolean emailExists(String email) {
        SqlRowSet rs = template.queryForRowSet(SQL_SELECT_USER_BY_EMAIL, email);
        
		return rs.next();
    }

    public Map<String,Integer> getEventCountPerUser() {
        SqlRowSet rs = template.queryForRowSet(SQL_COUNT_EVENTS_PER_USER);
        Map<String,Integer> eventsPerUser = new HashMap<>();

        while(rs.next()) {
            eventsPerUser.put(rs.getString("id"), rs.getInt("events_count"));
        }

        return eventsPerUser;
    }
}

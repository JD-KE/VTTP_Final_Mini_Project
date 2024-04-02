package com.jd.eventhall.MainAppBackend.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class EventRepo {
    @Autowired
    private JdbcTemplate jdbcTemplate;
}

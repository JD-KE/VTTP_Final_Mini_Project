package com.jd.eventhall.MainAppBackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jd.eventhall.MainAppBackend.repository.EventRepo;


@Service
public class EventService {
    @Autowired
    private EventRepo eventRepo;
}

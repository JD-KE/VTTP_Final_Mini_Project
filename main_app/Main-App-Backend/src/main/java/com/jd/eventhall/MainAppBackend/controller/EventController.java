package com.jd.eventhall.MainAppBackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jd.eventhall.MainAppBackend.service.EventService;


@RestController
@RequestMapping("/api/event")
// @CrossOrigin("*")
public class EventController {
    @Autowired
    private EventService eventSvc;

    @PostMapping("/create")
    public ResponseEntity<String> createEvent(@RequestBody String request) {
        System.out.println(request);
        
        return ResponseEntity.ok().body("{\"test\":\"true\"}");
    }
}

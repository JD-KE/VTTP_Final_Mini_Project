package com.jd.eventhall.MainAppBackend.controller;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jd.eventhall.MainAppBackend.config.JwtService;
import com.jd.eventhall.MainAppBackend.exception.EventModelException;
import com.jd.eventhall.MainAppBackend.model.Event;
import com.jd.eventhall.MainAppBackend.model.GameSummary;
import com.jd.eventhall.MainAppBackend.model.User;
import com.jd.eventhall.MainAppBackend.service.EventService;
import com.jd.eventhall.MainAppBackend.service.UserService;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;


@RestController
@RequestMapping("/api/event")
// @CrossOrigin("*")
public class EventController {

    @Autowired
    private EventService eventSvc;

    @Autowired
    private UserService userSvc;

    @Autowired
    private JwtService jwtSvc;

    @GetMapping("/user")
    public ResponseEntity<String> getUserEvents(
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        
        String authToken = authHeader.substring(7);
        String username = jwtSvc.extractUsername(authToken);

        List<Event> events = eventSvc.getEventsByUsername(username);

        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        for (Event event:events) {
            JsonArrayBuilder gamesBuilder = Json.createArrayBuilder();
            for (GameSummary game:event.getGames()) {
                gamesBuilder.add(Json.createObjectBuilder()
                    .add("id", game.getId())
                    .add("name", game.getName())
                    .add("coverUrl", game.getCoverUrl())
                );
            }
            arrayBuilder.add(Json.createObjectBuilder()
                .add("id", event.getId())
                .add("name", event.getName())
                .add("description", event.getDescription())
                .add("details", event.getDetails())
                .add("userCreated", username)
                .add("games", gamesBuilder)
                .add("startTime", event.getStartDate())
                .add("endTime", event.getEndDate())
                
            );
        }
        
        return ResponseEntity.ok().body(arrayBuilder.build().toString());
    }

    @GetMapping("/view/{id}")
    public ResponseEntity<String> getEventById(@PathVariable String id) {

        Event event;
        try {
            event = eventSvc.getEventById(id).orElseThrow();
        } catch(NoSuchElementException ex) {
            return ResponseEntity.notFound().build();
        }
        User user = userSvc.getUserById(event.getUserCreatedId()).get();

        JsonArrayBuilder gamesBuilder = Json.createArrayBuilder();
        for (GameSummary game:event.getGames()) {
            gamesBuilder.add(Json.createObjectBuilder()
                .add("id", game.getId())
                .add("name", game.getName())
                .add("coverUrl", game.getCoverUrl())
            );
        }

        JsonObject eventObject = Json.createObjectBuilder()
            .add("id", event.getId())
            .add("name", event.getName())
            .add("description", event.getDescription())
            .add("details", event.getDetails())
            .add("userCreated", user.getUsername())
            .add("games", gamesBuilder)
            .add("startTime", event.getStartDate())
            .add("endTime", event.getEndDate())
            .build();

        return ResponseEntity.ok().body(eventObject.toString());
    }

    @PostMapping("/create")
    public ResponseEntity<String> createEvent(@RequestBody String event,
    @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        // System.out.println(event);
        JsonReader reader = Json.createReader(new StringReader(event));
        JsonObject eventObject = reader.readObject();
        JsonObjectBuilder builder = Json.createObjectBuilder();
        User user;
        List<GameSummary> games;

        try {
            user = userSvc.getUserByUsername(eventObject.getString("userCreated"))
                .orElseThrow();
        } catch (NoSuchElementException ex) {

            return ResponseEntity.badRequest().body(
                Json.createObjectBuilder()
                    .add("message", "User not found, event creation cancelled")
                    .build()
                    .toString()
            );
        }

        String authToken = authHeader.substring(7);
        String username = jwtSvc.extractUsername(authToken);
        if(!username.equals(user.getUsername())) {
            return ResponseEntity.badRequest().body(
                Json.createObjectBuilder()
                    .add("message", "User does not match authorized user, event creation cancelled")
                    .build()
                    .toString()
            );
        }

        games = eventObject.getJsonArray("games").stream()
            .map(jv -> {
                JsonObject jsonGame = jv.asJsonObject();
                return GameSummary.builder()
                    .id(jsonGame.getInt("id"))
                    .name(jsonGame.getString("name"))
                    .coverUrl(jsonGame.getString("coverUrl", ""))
                    .build(); 
            })
            .toList();
        

        Event eventToCreate = Event.builder()
            .name(eventObject.getString("name"))
            .description(eventObject.getString("description"))
            .details(eventObject.getString("details"))
            .startDate(eventObject.getJsonNumber("startTime").longValueExact())
            .endDate(eventObject.getJsonNumber("endTime").longValueExact())
            .userCreatedId(user.getId())
            .games(games)
            .build();
        
        String eventId;
        try{
            eventId = eventSvc.createEvent(eventToCreate);
        } catch (EventModelException ex) {
            builder.add("message", ex.getMessage());
            return ResponseEntity.status(400).body(builder.build().toString());
        }
        builder.add("eventId", eventId);
        
        return ResponseEntity.status(201).body(builder.build().toString());
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateEvent(@RequestBody String event,
    @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        // System.out.println(event);
        JsonReader reader = Json.createReader(new StringReader(event));
        JsonObject eventObject = reader.readObject();
        JsonObjectBuilder builder = Json.createObjectBuilder();
        User user;
        List<GameSummary> games;

        try {
            user = userSvc.getUserByUsername(eventObject.getString("userCreated"))
                .orElseThrow();
        } catch (NoSuchElementException ex) {

            return ResponseEntity.badRequest().body(
                Json.createObjectBuilder()
                    .add("message", "User not found, event update cancelled")
                    .build()
                    .toString()
            );
        }

        String authToken = authHeader.substring(7);
        String username = jwtSvc.extractUsername(authToken);
        if(!username.equals(user.getUsername())) {
            return ResponseEntity.badRequest().body(
                Json.createObjectBuilder()
                    .add("message", "User does not match authorized user, event update cancelled")
                    .build()
                    .toString()
            );
        }

        games = eventObject.getJsonArray("games").stream()
            .map(jv -> {
                JsonObject jsonGame = jv.asJsonObject();
                return GameSummary.builder()
                    .id(jsonGame.getInt("id"))
                    .name(jsonGame.getString("name"))
                    .coverUrl(jsonGame.getString("coverUrl", ""))
                    .build(); 
            })
            .toList();

        Event eventToUpdate = Event.builder()
            .id(eventObject.getString("id"))
            .name(eventObject.getString("name"))
            .description(eventObject.getString("description"))
            .details(eventObject.getString("details"))
            .startDate(eventObject.getJsonNumber("startTime").longValueExact())
            .endDate(eventObject.getJsonNumber("endTime").longValueExact())
            // .userCreatedId(user.getId())
            .games(games)
            .build();
        
        // String eventId;
        try{
            eventSvc.updateEvent(eventToUpdate);
        } catch (EventModelException ex) {
            builder.add("message", ex.getMessage());
            return ResponseEntity.status(400).body(builder.build().toString());
        }
        builder.add("eventId", eventToUpdate.getId());
        
        return ResponseEntity.status(201).body(builder.build().toString());
    }
}

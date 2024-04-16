package com.jd.eventhall.MainAppBackend.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jd.eventhall.MainAppBackend.Utils.GenerateId;
import com.jd.eventhall.MainAppBackend.exception.EventModelException;
import com.jd.eventhall.MainAppBackend.model.Event;
import com.jd.eventhall.MainAppBackend.model.GameSummary;
import com.jd.eventhall.MainAppBackend.model.User;
import com.jd.eventhall.MainAppBackend.repository.EventRepo;
import com.jd.eventhall.MainAppBackend.repository.UserRepo;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;


@Service
public class EventService {
    
    @Autowired
    private EventRepo eventRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private GameService gameSvc;

    @Autowired
    private MeterRegistry meterRegistry;

    @PostConstruct
    public void buildGauge () {
        Gauge.builder("current_events_count", eventRepo::countEvents)
            .description("Current number of events")
            .baseUnit("Events")
            .register(meterRegistry);
        
            Gauge.builder("max_games_in_event", eventRepo::getMaxGamesInEvent)
            .description("Maximum number of games in a single event currently. Custom metric.")
            .baseUnit("Games")
            .register(meterRegistry);
    }

    public List<Event> getEventsByUsername(String username) {
        User user = userRepo.getUserByUsername(username).get();
        
        List<Event> events = eventRepo.getEventsByUserId(user.getId());

        for (Event event:events) {

            event.setGames(eventRepo.getGameSummaryByEventId(event.getId()));

        }


        return events;
    }

    public List<Event> getEventsByUsername(String username, int page, int limit) {
        User user = userRepo.getUserByUsername(username).get();
        
        List<Event> events = eventRepo.getEventsByUserId(user.getId(), page, limit);

        for (Event event:events) {

            event.setGames(eventRepo.getGameSummaryByEventId(event.getId()));

        }

        return events;
    }

    public int getEventCountByUsername(String username) {
        User user = userRepo.getUserByUsername(username).get();
        return eventRepo.countEvents(user.getId());
    }

    public Optional<Event> getEventById(String id) {
        Event event;
        try{
            event = eventRepo.getEventsById(id).orElseThrow();
        } catch(NoSuchElementException ex) {
            return Optional.empty();
        }

        event.setGames(eventRepo.getGameSummaryByEventId(id));

        return Optional.of(event);
    }

    @Transactional(rollbackFor = EventModelException.class)
    public String createEvent(Event event) throws EventModelException {
        String id = GenerateId.generateId();
        while(eventRepo.idExists(id)) {
            id = GenerateId.generateId();
        }
        event.setId(id);

        eventRepo.createEvent(event);
        
        for (GameSummary game: event.getGames()) {
            String gameDbId = GenerateId.generateId();
            while(eventRepo.eventGameDbIdExists(gameDbId)) {
                gameDbId = GenerateId.generateId();
            }
            eventRepo.createEventGame(gameDbId,game,event.getId());
        }

        return event.getId();
    }

    @Transactional(rollbackFor = EventModelException.class)
    public void updateEvent(Event event) throws EventModelException {
        eventRepo.updateEvent(event);
        
        eventRepo.deleteEventGames(event.getId());

        for (GameSummary game: event.getGames()) {
            String gameDbId = GenerateId.generateId();
            while(eventRepo.eventGameDbIdExists(gameDbId)) {
                gameDbId = GenerateId.generateId();
            }
            eventRepo.createEventGame(gameDbId,game,event.getId());
        }
    }
}

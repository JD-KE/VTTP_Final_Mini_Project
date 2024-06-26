package com.jd.eventhall.MainAppBackend.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.jd.eventhall.MainAppBackend.exception.EventModelException;
import com.jd.eventhall.MainAppBackend.model.Event;
import com.jd.eventhall.MainAppBackend.model.GameSummary;

@Repository
public class EventRepo {
    
    @Autowired
    private JdbcTemplate template;

    public static final String SQL_COUNT_ALL_EVENTS = """
            select count(*) from events
            """;

    public static final String SQL_COUNT_USER_EVENTS = """
        select count(*) from events where user_created like ?
        """;

    public static final String SQL_SELECT_EVENT_BY_ID = """
            select * from events where id like ?
            """;

    public static final String SQL_SELECT_EVENT_BY_USER_ID = """
        select * from events where user_created like ? order by date_start desc, date_end desc
        """;

    public static final String SQL_SELECT_EVENT_BY_USER_ID_PAGINATED = """
        select * from events where user_created like ? order by date_start desc, date_end desc limit ? offset ?
        """;

    public static final String SQL_SELECT_EVENT_GAME_BY_ID = """
        select * from eventgames where id like ?
        """;
    
    public static final String SQL_SELECT_EVENT_GAME_BY_EVENT_ID = """
        select * from eventgames where event_id like ?
        """;
    
    public static final String SQL_INSERT_EVENT = """
            insert into events(id,name,description,details,date_start,date_end,user_created) values(?,?,?,?,?,?,?)
            """;

    public static final String SQL_INSERT_EVENT_GAME = """
            insert into eventgames(id,game_id,game_name,game_cover_url,event_id) values(?,?,?,?,?)
            """;

    public static final String SQL_DELETE_EVENT_GAMES = """
            delete from eventgames where event_id = ?
            """;
    
    public static final String SQL_UPDATE_EVENT = """
            update events set
                name = ?,
                description = ?,
                details = ?,
                date_start = ?,
                date_end = ?
                where id = ?
            """;

    public static final String SQL_COUNT_GAMES_PER_EVENT = """
        select event_id, count(event_id)
            from eventgames
            group by event_id;
            """;
    public static final String SQL_MAX_GAMES_IN_EVENT = """
        select max(event_games_per_event.games_count) from(
select event_id, count(event_id) as games_count
	from eventgames
    group by event_id
) as event_games_per_event;
            """;

    public Optional<Event> getEventsById(String id) {
        SqlRowSet rs = template.queryForRowSet(SQL_SELECT_EVENT_BY_ID, id);
        Event event;
        
        if(rs.next()) {
            event = Event.builder()
                .id(rs.getString("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .details(rs.getString("details"))
                .startDate(rs.getLong("date_start"))
                .endDate(rs.getLong("date_end"))
                .userCreatedId(rs.getString("user_created"))
                .build();
            return Optional.of(event);
        }

        return Optional.empty();
    }
    public List<Event> getEventsByUserId(String id) {
        SqlRowSet rs = template.queryForRowSet(SQL_SELECT_EVENT_BY_USER_ID, id);
        List<Event> events = new ArrayList<>();
        while(rs.next()) {
            Event event = Event.builder()
                .id(rs.getString("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .details(rs.getString("details"))
                .startDate(rs.getLong("date_start"))
                .endDate(rs.getLong("date_end"))
                .userCreatedId(rs.getString("user_created"))
                .build();
            events.add(event);
        }

        return events;
    }

    public List<Event> getEventsByUserId(String id, int page, int limit) {
        SqlRowSet rs = template.queryForRowSet(SQL_SELECT_EVENT_BY_USER_ID_PAGINATED,
         id, limit, (page-1)*limit);
        List<Event> events = new ArrayList<>();
        while(rs.next()) {
            Event event = Event.builder()
                .id(rs.getString("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .details(rs.getString("details"))
                .startDate(rs.getLong("date_start"))
                .endDate(rs.getLong("date_end"))
                .userCreatedId(rs.getString("user_created"))
                .build();
            events.add(event);
        }

        return events;
    }

    public List<GameSummary> getGameSummaryByEventId(String id) {
        SqlRowSet rs = template.queryForRowSet(SQL_SELECT_EVENT_GAME_BY_EVENT_ID, id);
        List<GameSummary> gameSummaries = new ArrayList<>();
        while(rs.next()) {
            GameSummary gameSummary = GameSummary.builder()
                .id(rs.getInt("game_id"))
                .name(rs.getString("game_name"))
                .coverUrl(rs.getString("game_cover_url"))
                .build();
            gameSummaries.add(gameSummary);
        }

        return gameSummaries;
    }

    public void createEvent(Event event) throws EventModelException {
        int rowsUpdated = template.update(SQL_INSERT_EVENT,
            event.getId(),
            event.getName(),
            event.getDescription(),
            event.getDetails(),
            event.getStartDate(),
            event.getEndDate(),
            event.getUserCreatedId());

        if (rowsUpdated <= 0) throw new EventModelException("Event cannot be created");
    }

    public void updateEvent(Event event) throws EventModelException {
        int rowsUpdated = template.update(SQL_UPDATE_EVENT,
            event.getName(),
            event.getDescription(),
            event.getDetails(),
            event.getStartDate(),
            event.getEndDate(),
            event.getId()
        );
        if (rowsUpdated <= 0) throw new EventModelException("Event cannot be updated");
    }

    public void createEventGame(String id, GameSummary game, String eventId) throws EventModelException {
        int rowsUpdated = template.update(SQL_INSERT_EVENT_GAME,
            id, 
            game.getId(), 
            game.getName(),
            game.getCoverUrl(),
            eventId);
        if (rowsUpdated <= 0) throw new EventModelException("Event games cannot be saved");
    }

    public void deleteEventGames(String eventId) throws EventModelException   {
        int rowsUpdated = template.update(SQL_DELETE_EVENT_GAMES, eventId);

        if (rowsUpdated <= 0) throw new EventModelException("Old event games was not deleted during event update");
    }

    public boolean idExists(String id) {
        SqlRowSet rs = template.queryForRowSet(SQL_SELECT_EVENT_BY_ID, id);
        
		return rs.next();
    }

    public boolean eventGameDbIdExists(String gameDbId) {
        SqlRowSet rs = template.queryForRowSet(SQL_SELECT_EVENT_GAME_BY_ID, gameDbId);
        
		return rs.next();
    }

    public int countEvents() {
        SqlRowSet rs = template.queryForRowSet(SQL_COUNT_ALL_EVENTS);
        rs.next();
        return rs.getInt(1);
    }

    public int countEvents(String userId) {
        SqlRowSet rs = template.queryForRowSet(SQL_COUNT_USER_EVENTS, userId);
        rs.next();
        return rs.getInt(1);
    }

    public Map<String, Integer> getGamesCountPerEvent() {
        SqlRowSet rs = template.queryForRowSet(SQL_COUNT_GAMES_PER_EVENT);
        Map<String,Integer> gamesPerEvent = new HashMap<>();

        while(rs.next()) {
            gamesPerEvent.put(rs.getString("event_id"), rs.getInt("games_count"));
        }

        return gamesPerEvent;
    }

    public int getMaxGamesInEvent() {
        SqlRowSet rs = template.queryForRowSet(SQL_MAX_GAMES_IN_EVENT);
        rs.next();
        return rs.getInt(1);
    }


}

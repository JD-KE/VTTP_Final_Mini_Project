package com.jd.eventhall.igdblocal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jd.eventhall.igdblocal.service.GameService;

import jakarta.json.Json;


@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class GameController {
    @Autowired
    private GameService gameService;
    
    @GetMapping("/game/search")
    public ResponseEntity<String> searchGame(@RequestParam String searchTerm,
    @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int limit) {

        if (searchTerm.isBlank()){
            return ResponseEntity.ok(Json.createObjectBuilder()
                .add("results", Json.createArrayBuilder().build())
                .build().toString());
        }
        String resp = gameService.getGameResultsJsonString(searchTerm, page, limit);
        return ResponseEntity.ok().body(resp);
    }

    @GetMapping("game/{id}")
    public ResponseEntity<String> displayGame(@PathVariable int id) {
        if (gameService.gameExists(id)) {
            String resp = gameService.getGameById(id);
            return ResponseEntity.ok().body(resp);
        }

        return ResponseEntity.notFound().build();
        
    }

}

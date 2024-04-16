package com.jd.eventhall.MainAppBackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jd.eventhall.MainAppBackend.service.GameService;

import jakarta.json.Json;

@RestController
@RequestMapping("/api/game")
public class GameController {

    @Autowired
    private GameService gameService;

    
    @GetMapping("/search")
    public ResponseEntity<String> searchGame(@RequestParam String searchTerm,
    @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int limit) {
        if (page<=0 || limit <=0) {
            // System.out.println("No search term");
            return ResponseEntity.badRequest().build();
        }

        if (searchTerm.isBlank()){
            return ResponseEntity.ok(Json.createObjectBuilder()
                .add("results", Json.createArrayBuilder().build())
                .build().toString());
        }

        String resp = gameService.getGameResultsJsonString(searchTerm, page, limit);
        return ResponseEntity.ok().body(resp);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> displayGame(@PathVariable int id) {
        if (gameService.gameExists(id)) {
            String resp = gameService.getGameById(id);
            return ResponseEntity.ok().body(resp);
        }

        return ResponseEntity.notFound().build();
        
    }

}

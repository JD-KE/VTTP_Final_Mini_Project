package com.jd.eventhall.igdblocal.controller;

import java.io.StringReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jd.eventhall.igdblocal.repository.CoverRepo;
import com.jd.eventhall.igdblocal.repository.GameRepo;
import com.jd.eventhall.igdblocal.repository.PlatformRepo;
import com.jd.eventhall.igdblocal.repository.ReleaseDateRepo;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@RestController
@RequestMapping("/webhook")
@CrossOrigin
public class WebhookController {
    @Autowired
    private GameRepo gameRepo;

    @Autowired 
    private CoverRepo coverRepo;

    @Autowired
    private ReleaseDateRepo releaseDateRepo;

    @Autowired
    private PlatformRepo platformRepo;

    @PostMapping(path = "/game/create")
    public ResponseEntity<String> IGDBCreateGame(@RequestBody String gameString, 
    @RequestHeader("X-Secret") String x_secret) {
        JsonReader jsonReader = Json.createReader(new StringReader(gameString));
        JsonObject jsonObject = jsonReader.readObject();
        
        System.out.println("Game %s: %s".formatted(jsonObject.getInt("id"),
            jsonObject.getString("name")));
        boolean inserted = gameRepo.insertGame(jsonObject);
        if(inserted) System.out.println("inserted");
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/game/update")
    public ResponseEntity<String> IGDBUpdateGame(@RequestBody String gameString, 
    @RequestHeader("X-Secret") String x_secret) {
        JsonReader jsonReader = Json.createReader(new StringReader(gameString));
        JsonObject jsonObject = jsonReader.readObject();
        
        System.out.println("Game %s: %s".formatted(jsonObject.getInt("id"),
            jsonObject.getString("name")));
        boolean updated = gameRepo.updateGame(jsonObject);
        if(updated) System.out.println("updated");
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/game/delete")
    public ResponseEntity<String> IGDBDeleteGame(@RequestBody String idString, 
    @RequestHeader("X-Secret") String x_secret) {
        JsonReader jsonReader = Json.createReader(new StringReader(idString));
        JsonObject jsonObject = jsonReader.readObject();
        int id = jsonObject.getInt("id");
        // System.out.println("Receiving from delete webhook:");
        System.out.println("Game %s".formatted(id));
        boolean deleted = gameRepo.deleteGame(id);
        if(deleted) System.out.println("deleted");
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/cover/create")
    public ResponseEntity<String> IGDBCreateCover(@RequestBody String coverString, 
    @RequestHeader("X-Secret") String x_secret) {
        JsonReader jsonReader = Json.createReader(new StringReader(coverString));
        JsonObject jsonObject = jsonReader.readObject();
        
        System.out.println("Cover %s: %s".formatted(jsonObject.getInt("id"),
            jsonObject.getString("image_id")));
        boolean inserted = coverRepo.insertCover(jsonObject);
        if(inserted) System.out.println("inserted");
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/cover/update")
    public ResponseEntity<String> IGDBUpdateCover(@RequestBody String coverString, 
    @RequestHeader("X-Secret") String x_secret) {
        JsonReader jsonReader = Json.createReader(new StringReader(coverString));
        JsonObject jsonObject = jsonReader.readObject();
        
        System.out.println("Cover %s: %s".formatted(jsonObject.getInt("id"),
            jsonObject.getString("image_id")));
        boolean updated = coverRepo.updateCover(jsonObject);
        if(updated) System.out.println("updated");
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/cover/delete")
    public ResponseEntity<String> IGDBDeleteCover(@RequestBody String idString, 
    @RequestHeader("X-Secret") String x_secret) {
        JsonReader jsonReader = Json.createReader(new StringReader(idString));
        JsonObject jsonObject = jsonReader.readObject();
        int id = jsonObject.getInt("id");
        // System.out.println("Receiving from delete webhook:");
        System.out.println("Cover %s".formatted(id));
        boolean deleted = coverRepo.deleteCover(id);
        if(deleted) System.out.println("deleted");
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/release_date/create")
    public ResponseEntity<String> IGDBCreateReleaseDate(@RequestBody String releaseDate, 
    @RequestHeader("X-Secret") String x_secret) {
        JsonReader jsonReader = Json.createReader(new StringReader(releaseDate));
        JsonObject jsonObject = jsonReader.readObject();
        
        System.out.println("Release Date %s".formatted(jsonObject.getInt("id")));
        boolean inserted = releaseDateRepo.insertReleaseDate(jsonObject);
        if(inserted) System.out.println("inserted");
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/release_date/update")
    public ResponseEntity<String> IGDBUpdateReleaseDate(@RequestBody String releaseDate, 
    @RequestHeader("X-Secret") String x_secret) {
        JsonReader jsonReader = Json.createReader(new StringReader(releaseDate));
        JsonObject jsonObject = jsonReader.readObject();
        
        System.out.println("Release Date %s".formatted(jsonObject.getInt("id")));
        boolean updated = releaseDateRepo.updateReleaseDate(jsonObject);
        if(updated) System.out.println("updated");
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/release_date/delete")
    public ResponseEntity<String> IGDBDeleteReleaseDate(@RequestBody String idString, 
    @RequestHeader("X-Secret") String x_secret) {
        JsonReader jsonReader = Json.createReader(new StringReader(idString));
        JsonObject jsonObject = jsonReader.readObject();
        int id = jsonObject.getInt("id");
        // System.out.println("Receiving from delete webhook:");
        System.out.println("Release Date %s".formatted(id));
        boolean deleted = releaseDateRepo.deleteReleaseDate(id);
        if(deleted) System.out.println("deleted");
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/platform/create")
    public ResponseEntity<String> IGDBCreatePlatform(@RequestBody String coverString, 
    @RequestHeader("X-Secret") String x_secret) {
        JsonReader jsonReader = Json.createReader(new StringReader(coverString));
        JsonObject jsonObject = jsonReader.readObject();
        
        System.out.println("Platform %s: %s".formatted(jsonObject.getInt("id"),
            jsonObject.getString("name")));
        boolean inserted = platformRepo.insertPlatform(jsonObject);
        if(inserted) System.out.println("inserted");
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/platform/update")
    public ResponseEntity<String> IGDBUpdatePlatform(@RequestBody String coverString, 
    @RequestHeader("X-Secret") String x_secret) {
        JsonReader jsonReader = Json.createReader(new StringReader(coverString));
        JsonObject jsonObject = jsonReader.readObject();
        
        System.out.println("Platform %s: %s".formatted(jsonObject.getInt("id"),
            jsonObject.getString("name")));
        boolean updated = platformRepo.updatePlatform(jsonObject);
        if(updated) System.out.println("updated");
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/platform/delete")
    public ResponseEntity<String> IGDBDeletePlatform(@RequestBody String idString, 
    @RequestHeader("X-Secret") String x_secret) {
        JsonReader jsonReader = Json.createReader(new StringReader(idString));
        JsonObject jsonObject = jsonReader.readObject();
        int id = jsonObject.getInt("id");
        // System.out.println("Receiving from delete webhook:");
        System.out.println("Platform %s".formatted(id));
        boolean deleted = platformRepo.deletePlatform(id);
        if(deleted) System.out.println("deleted");
        return ResponseEntity.ok().build();
    }
}

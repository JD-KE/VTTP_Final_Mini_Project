package com.jd.eventhall.MainAppBackend.service;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jd.eventhall.MainAppBackend.repository.CoverRepo;
import com.jd.eventhall.MainAppBackend.repository.GameRepo;
import com.jd.eventhall.MainAppBackend.repository.PlatformRepo;
import com.jd.eventhall.MainAppBackend.repository.ReleaseDateRepo;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;

@Service
public class GameService {
    @Autowired
    private GameRepo gameRepo;

    @Autowired 
    private CoverRepo coverRepo;

    @Autowired
    private ReleaseDateRepo releaseDateRepo;

    @Autowired
    private PlatformRepo platformRepo;

    public String getGameResultsJsonString(String searchTerm, int page, int limit) {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        jsonObjectBuilder.add("totalCount", gameRepo.searchGameByNameCount(searchTerm));
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();

        List<Document> results = gameRepo.testSearchGamesByName(searchTerm,page,limit);
        for(Document result: results) {
            JsonObjectBuilder jsonObjectBuilderResults = Json.createObjectBuilder();
            jsonObjectBuilderResults.add("id", result.getInteger("_id"))
                .add("name", result.getString("name"))
                .add("category", result.getInteger("category"))
                .add("coverUrl", coverRepo.getUrlFromId(result.getInteger("cover")));

            long date;
            if((date = result.getDate("first_release_date").getTime()) != 0) {
                jsonObjectBuilderResults.add("first_release_date", date);
            }

            int parentId;
            if((parentId = result.getInteger("parent_game")) != 0) {
                Document doc = gameRepo.getGameById(parentId);
                JsonObjectBuilder parentObjectBuilder = Json.createObjectBuilder()
                    .add("id", parentId)
                    .add("name", doc.getString("name"));
                    if((date = doc.getDate("first_release_date").getTime()) != 0) {
                        parentObjectBuilder.add("first_release_date", date);
                    }
                jsonObjectBuilderResults.add("parent_game",
                    parentObjectBuilder);
                
            }
           
            jsonArrayBuilder.add(jsonObjectBuilderResults);
        }

        jsonObjectBuilder.add("results", jsonArrayBuilder);

        return jsonObjectBuilder.build().toString();
    }

    public boolean gameExists(int id) {
        return gameRepo.gameExists(id);
    }

    public String getGameById(int id) {
        Document result = gameRepo.getGameById(id);
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder()
            .add("id",result.getInteger("_id"))
            .add("name", result.getString("name"))
            .add("category", result.getInteger("category"))
            .add("status", result.getInteger("status"))
            .add("storyline", result.getString("storyline"))
            .add("summary", result.getString("summary"))
            .add("coverUrl", coverRepo.getUrlFromId(result.getInteger("cover")));
        long date;
        if((date = result.getDate("first_release_date").getTime()) != 0) {
            jsonObjectBuilder.add("first_release_date", date);
        }
        int parentId;
        if((parentId = result.getInteger("parent_game")) != 0) {
            Document doc = gameRepo.getGameById(parentId);
            JsonObjectBuilder parentObjectBuilder = Json.createObjectBuilder()
                .add("id", parentId)
                .add("name", doc.getString("name"));
                if((date = doc.getDate("first_release_date").getTime()) != 0) {
                    parentObjectBuilder.add("first_release_date", date);
                }
            jsonObjectBuilder.add("parent_game",
                parentObjectBuilder);
        }
        int versionParentId;
        if((versionParentId = result.getInteger("version_parent")) != 0) {
            Document doc = gameRepo.getGameById(versionParentId);
            JsonObjectBuilder vParentObjectBuilder = Json.createObjectBuilder()
                .add("id", parentId)
                .add("name", doc.getString("name"));
                if((date = doc.getDate("first_release_date").getTime()) != 0) {
                    vParentObjectBuilder.add("first_release_date", date);
                }
            jsonObjectBuilder.add("version_parent",
                vParentObjectBuilder);
            jsonObjectBuilder.add("version_title", result.getString("version_title"));
        }
        return jsonObjectBuilder.build().toString();
    }

    
    
}

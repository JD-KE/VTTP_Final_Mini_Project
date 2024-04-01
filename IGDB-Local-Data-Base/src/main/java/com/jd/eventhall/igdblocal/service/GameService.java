package com.jd.eventhall.igdblocal.service;

import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.api.igdb.apicalypse.APICalypse;
import com.api.igdb.exceptions.RequestException;
import com.api.igdb.request.IGDBWrapper;
import com.api.igdb.request.JsonRequestKt;
import com.api.igdb.request.ProtoRequestKt;
import com.jd.eventhall.igdblocal.repository.CoverRepo;
import com.jd.eventhall.igdblocal.repository.GameRepo;
import com.jd.eventhall.igdblocal.repository.PlatformRepo;
import com.jd.eventhall.igdblocal.repository.ReleaseDateRepo;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import proto.Cover;
import proto.Game;
import proto.Platform;
import proto.ReleaseDate;

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

    @Value("${application.igdb.proxy.url}")
    private String proxyUrl;

    @Value("${application.igdb.proxy.api-key}")
    private String x_api_key;


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

    public void testMethod() {
        IGDBWrapper wrapper = IGDBWrapper.INSTANCE;
        Map<String, String> proxyHeaders = new HashMap<String, String>() {{
            put("x-api-key", x_api_key);
        }};
        wrapper.setupProxy(proxyUrl, proxyHeaders);
        
        APICalypse apiCalypse = new APICalypse().fields("name,url,cover.*").limit(10);

        try {
            List<Game> games = ProtoRequestKt.games(wrapper,apiCalypse);
            System.out.println(games.size());
            Game game = games.get(0);
            System.out.println(game.toString());
            System.out.println(game.getUrl());
            System.out.println(game.getCover().getUrl());
        } catch (RequestException e) {
            e.printStackTrace();
        }
    }

    public void createGameDB() {
        IGDBWrapper wrapper = IGDBWrapper.INSTANCE;
        Map<String, String> proxyHeaders = new HashMap<String, String>() {{
            put("x-api-key", x_api_key);
        }};
        wrapper.setupProxy(proxyUrl, proxyHeaders);

        boolean flag = true;
        int loops = 0;
        gameRepo.deleteCollection();
        System.out.println("Starting game collection creation");
        do {
            APICalypse apiCalypse = new APICalypse()
            .fields("name,category,status,parent_game,version_parent,version_title,aggregated_rating,rating,storyline,summary,first_release_date,cover,platforms,release_dates")
            .limit(500).offset(loops*500);
            try {
                String gamesString = JsonRequestKt.jsonGames(wrapper, apiCalypse);
                JsonReader jsonReader = Json.createReader(new StringReader(gamesString));
                JsonArray jsonArray = jsonReader.readArray();
                
                List<JsonObject> jsonGames = jsonArray.stream()
                    .map(jv -> jv.asJsonObject())
                    .toList();

                if(jsonGames.size() != 0) {
                    for (JsonObject game: jsonGames) {
                        gameRepo.updateGame(game);
                    }
                } else {
                    flag = false;
                }

            } catch (RequestException e) {
                System.out.println(e.toString());
                e.printStackTrace();
                
            }
            loops++;
            System.out.println("Game loops: %d".formatted(loops));
            
            try {
                Thread.sleep(400l);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while(flag);

        System.out.println(gameRepo.collectionCount());
        gameRepo.collectionCreateTextIndex();

    }
    

    public void createCoverDB() {
        IGDBWrapper wrapper = IGDBWrapper.INSTANCE;
        Map<String, String> proxyHeaders = new HashMap<String, String>() {{
            put("x-api-key", x_api_key);
        }};
        wrapper.setupProxy(proxyUrl, proxyHeaders);

        boolean flag = true;
        int loops = 0;

        System.out.println("Starting cover collection creation");
        coverRepo.testDeleteCollection();

        do {
            APICalypse apiCalypse = new APICalypse()
            .fields("game,image_id,url")
            .limit(500).offset(loops*500);
            try {
                String coversString = JsonRequestKt.jsonCovers(wrapper, apiCalypse);
                JsonReader jsonReader = Json.createReader(new StringReader(coversString));
                JsonArray jsonArray = jsonReader.readArray();
                
                List<JsonObject> jsonCovers = jsonArray.stream()
                    .map(jv -> jv.asJsonObject())
                    .toList();

                if(jsonCovers.size() != 0) {
                    for (JsonObject game: jsonCovers) {
                        coverRepo.updateCover(game);
                    }
                } else {
                    flag = false;
                }

            } catch (RequestException e) {
                System.out.println(e.toString());
                e.printStackTrace();
                
            }
            loops++;
            System.out.println("Cover loops: %d".formatted(loops));
            
            try {
                Thread.sleep(400l);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while(flag);

        System.out.println(coverRepo.collectionCount());
    }

    public void createReleaseDateDB() {
        IGDBWrapper wrapper = IGDBWrapper.INSTANCE;
        Map<String, String> proxyHeaders = new HashMap<String, String>() {{
            put("x-api-key", x_api_key);
        }};
        wrapper.setupProxy(proxyUrl, proxyHeaders);

        boolean flag = true;
        int loops = 0;

        System.out.println("Starting Release Date collection creation");
        releaseDateRepo.deleteCollection();
        flag = true;
        loops = 0;

        do {
            APICalypse apiCalypse = new APICalypse()
            .fields("date,game,human,platform,region")
            .limit(500).offset(loops*500);
            try {

                String rdsString = JsonRequestKt.jsonReleaseDates(wrapper, apiCalypse);
                JsonReader jsonReader = Json.createReader(new StringReader(rdsString));
                JsonArray jsonArray = jsonReader.readArray();
                
                List<JsonObject> jsonRds = jsonArray.stream()
                    .map(jv -> jv.asJsonObject())
                    .toList();

                if(jsonRds.size() != 0) {
                    for (JsonObject game: jsonRds) {
                        releaseDateRepo.updateReleaseDate(game);
                    }
                } else {
                    flag = false;
                }
            
            } catch (RequestException e) {
                System.out.println(e.toString());
                e.printStackTrace();
                
            }
            loops++;
            System.out.println("ReleaseDate loops: %d".formatted(loops));
            
            try {
                Thread.sleep(400l);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while(flag);

        System.out.println(releaseDateRepo.collectionCount());
    }

    public void createPlatformDB() {
        IGDBWrapper wrapper = IGDBWrapper.INSTANCE;
        Map<String, String> proxyHeaders = new HashMap<String, String>() {{
            put("x-api-key", x_api_key);
        }};
        wrapper.setupProxy(proxyUrl, proxyHeaders);

        boolean flag = true;
        int loops = 0;

        System.out.println("Starting platform collection creation");
        platformRepo.deleteCollection();
        flag = true;
        loops = 0;

        do {
            APICalypse apiCalypse = new APICalypse()
            .fields("name")
            .limit(500).offset(loops*500);
            try {
                String platformsString = JsonRequestKt.jsonPlatforms(wrapper, apiCalypse);
                JsonReader jsonReader = Json.createReader(new StringReader(platformsString));
                JsonArray jsonArray = jsonReader.readArray();
                
                List<JsonObject> jsonPlatforms = jsonArray.stream()
                    .map(jv -> jv.asJsonObject())
                    .toList();

                if(jsonPlatforms.size() != 0) {
                    for (JsonObject game: jsonPlatforms) {
                        platformRepo.updatePlatform(game);
                    }
                } else {
                    flag = false;
                }
            } catch (RequestException e) {
                System.out.println(e.toString());
                e.printStackTrace();
                
            }
            loops++;
            System.out.println("Platform loops: %d".formatted(loops));
            
            try {
                Thread.sleep(400l);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while(flag);

        System.out.println(platformRepo.collectionCount());
    }
}

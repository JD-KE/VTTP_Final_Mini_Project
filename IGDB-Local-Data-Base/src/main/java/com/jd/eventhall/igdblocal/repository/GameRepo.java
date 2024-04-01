package com.jd.eventhall.igdblocal.repository;

import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.TextIndexDefinition;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import jakarta.json.JsonNumber;
import jakarta.json.JsonObject;
import proto.Game;

@Repository
public class GameRepo {
    @Autowired
    private MongoTemplate mongoTemplate;
    
    public List<Document> testSearchGamesByName(String searchString, int page, int limit) {
        Query query = new TextQuery(TextCriteria.forDefaultLanguage().matchingPhrase(searchString).caseSensitive(false))
            .sortByScore()
            // .with(Sort.by(Direction.DESC, "rating")
            // .and(Sort.by(Direction.DESC,"aggregated_rating")))
            .with(Sort.by(new Sort.Order(Direction.DESC, "rating"),
            new Sort.Order(Direction.DESC,"aggregated_rating"),
            new Sort.Order(Direction.DESC, "first_release_date")))
            .skip((page - 1)*limit).limit(limit);
        
        // Query query = new Query(Criteria.where("name").regex(searchString, "i"));
        List<Document> docResults = mongoTemplate.find(query, Document.class, "game");
        
        return docResults;
    }

    public boolean gameExists(int id) {
        Query query = new Query(Criteria.where("_id").is(id));
        return mongoTemplate.exists(query, "game");
    }

    public Document getGameById(int id) {
        Query query = new Query(Criteria.where("_id").is(id));
        return mongoTemplate.findOne(query, Document.class, "game");
    }

    public long searchGameByNameCount(String searchString) {
        Query query = new TextQuery(TextCriteria.forDefaultLanguage().matchingPhrase(searchString));
        return mongoTemplate.count(query, "game");
    }

    public boolean collectionExists() {
        return mongoTemplate.collectionExists("game");
    }

    public void collectionCreateTextIndex() {
        mongoTemplate.indexOps("game")
        .ensureIndex(new TextIndexDefinition.TextIndexDefinitionBuilder()
            .onField("name")
            .build());
    }

    public Game insertGame(Game game) {
        Document doc = new Document("_id", game.getId())
        .append("name", game.getName())
        .append("category", game.getCategoryValue())
        .append("status", game.getStatusValue())
        .append("parent_game", game.getParentGame().getId())
        .append("cover", game.getCover().getId())
        .append("aggregated_rating", game.getAggregatedRating())
        .append("rating", game.getRating())
        .append("summary", game.getSummary())
        .append("first_release_date", new Date(game.getFirstReleaseDate().getSeconds()*1000));
        mongoTemplate.insert(doc, "game");
        System.out.println("Game inserted");
        return game;
    }

    public boolean insertGame(JsonObject game) {
        Document doc = new Document("_id", game.getInt("id"))
            .append("name", game.getString("name"))
            .append("category", game.getInt("category",0))
            .append("status", game.getInt("status", 1))
            .append("parent_game", game.getInt("parent_game",0))
            .append("version_parent", game.getInt("version_parent",0))
            .append("version_title", game.getString("version_title", ""))
            .append("cover", game.getInt("cover", 0))
            .append("aggregated_rating", game.containsKey("aggregated_rating") 
            ? game.getJsonNumber("aggregated_rating").doubleValue(): 0.0)
            .append("rating", game.containsKey("rating") 
            ? game.getJsonNumber("rating").doubleValue(): 0.0)
            .append("storyline", game.getString("storyline", ""))
            .append("summary", game.getString("summary", ""))
            .append("first_release_date", new Date(game.containsKey("first_release_date")
             ? game.getJsonNumber("first_release_date").longValueExact()*1000: 0l))
            .append("lastUpdated", new Date());
        
        if(game.containsKey("release_dates")) {
            List<Integer> releaseDatesList = game.getJsonArray("release_dates")
            .getValuesAs(JsonNumber.class)
            .stream()
            .map(jn -> jn.intValue())
            .toList();
            doc.put("release_dates", releaseDatesList);
        }

        if(game.containsKey("platforms")) {
            List<Integer> platformsList = game.getJsonArray("platforms")
            .getValuesAs(JsonNumber.class)
            .stream()
            .map(jn -> jn.intValue())
            .toList();
            doc.put("platforms", platformsList);
        }
        
        Document insertedDoc = mongoTemplate.insert(doc, "game");
        // System.out.println("Game inserted");
        return insertedDoc.equals(doc);
    }

    public long collectionCount() {
        return mongoTemplate.count(new Query(), "game");
    }

    public Game updateGame(Game game) {
        // Document doc = new Document().toJson();
        Query query = new Query(Criteria.where("_id").is(game.getId()));
        Update update = new Update()
            .set("name", game.getName())
            .set("category", game.getCategoryValue())
            .set("status", game.getStatus())
            .set("parent_game", game.getParentGame().getId())
            .set("cover", game.getCover().getId())
            .set("aggregated_rating", game.getAggregatedRating())
            .set("rating", game.getRating())
            .set("summary", game.getSummary())
            .set("first_release_date", new Date(game.getFirstReleaseDate().getSeconds()*1000));
        mongoTemplate.upsert(query, update, "game");
        // System.out.println("Updated: %s records".formatted(Long.toString(result.getModifiedCount())));
        return game;
    }

    public boolean updateGame(JsonObject game) {
        // Document doc = new Document().toJson();
        
        Query query = new Query(Criteria.where("_id").is(game.getInt("id")));
        Update update = new Update()
            .set("name", game.getString("name"))
            .set("category", game.getInt("category",0))
            .set("status", game.getInt("status", 1))
            .set("parent_game", game.getInt("parent_game",0))
            .set("version_parent", game.getInt("version_parent",0))
            .set("version_title", game.getString("version_title", ""))
            .set("cover", game.getInt("cover", 0))
            .set("aggregated_rating", game.containsKey("aggregated_rating") 
            ? game.getJsonNumber("aggregated_rating").doubleValue(): 0.0)
            .set("rating", game.containsKey("rating") 
            ? game.getJsonNumber("rating").doubleValue(): 0.0)
            .set("storyline", game.getString("storyline", ""))
            .set("summary", game.getString("summary", ""))
            .set("first_release_date", new Date(game.containsKey("first_release_date") 
            ? game.getJsonNumber("first_release_date").longValueExact()*1000: 0l))
            .set("lastUpdated", new Date());

            if(game.containsKey("release_dates")) {
                List<Integer> releaseDatesList = game.getJsonArray("release_dates")
                .getValuesAs(JsonNumber.class)
                .stream()
                .map(jn -> jn.intValue())
                .toList();
                update.set("release_dates", releaseDatesList);
            }
    
            if(game.containsKey("platforms")) {
                List<Integer> platformsList = game.getJsonArray("platforms")
                .getValuesAs(JsonNumber.class)
                .stream()
                .map(jn -> jn.intValue())
                .toList();
                update.set("platforms", platformsList);
            }

        UpdateResult result = mongoTemplate.upsert(query, update, "game");
        // System.out.println("Updated: %s records".formatted(Long.toString(result.getModifiedCount())));
        return result.getMatchedCount()>0 || result.getUpsertedId()!=null;
    }

    public boolean deleteGame(int id) {
        Query query = new Query(Criteria.where("_id").is(id));
        DeleteResult result = mongoTemplate.remove(query, "game");
        return result.getDeletedCount()>0;
    }

    public boolean deleteCollection() {
        mongoTemplate.dropCollection("game");
        return true;
    }
    
}

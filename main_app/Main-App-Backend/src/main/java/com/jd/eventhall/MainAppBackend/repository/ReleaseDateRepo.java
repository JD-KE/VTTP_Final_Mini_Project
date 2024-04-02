package com.jd.eventhall.MainAppBackend.repository;

import java.util.Date;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import jakarta.json.JsonObject;

@Repository
public class ReleaseDateRepo {
    @Autowired
    private MongoTemplate mongoTemplate;

    // public boolean collectionExists() {
    //     return mongoTemplate.collectionExists("releaseDate");
    // }

    // public boolean insertReleaseDate(JsonObject releaseDate) {
    //     Document doc = new Document("_id", releaseDate.getInt("id"))
    //         .append("date", new Date(releaseDate.containsKey("date")
    //          ? releaseDate.getJsonNumber("date").longValueExact()*1000: 0l))
    //         .append("game", releaseDate.getInt("game", 0))
    //         .append("human", releaseDate.getString("human", ""))
    //         .append("platform", releaseDate.getInt("platform"))
    //         .append("region", releaseDate.getInt("region", 0))
    //         .append("lastUpdated", new Date());

    //     Document insertedDoc = mongoTemplate.insert(doc, "releaseDate");
    //     return insertedDoc.equals(doc);
    // }

    // public long collectionCount() {
    //     return mongoTemplate.count(new Query(), "releaseDate");
    // }

    // public boolean updateReleaseDate(JsonObject releaseDate) {
    //     // Document doc = new Document().toJson();
    //     Query query = new Query(Criteria.where("_id").is(releaseDate.getInt("id")));
    //     Update update = new Update()
    //         .set("date", new Date(releaseDate.containsKey("date")
    //          ? releaseDate.getJsonNumber("date").longValueExact()*1000: 0l))
    //         .set("game", releaseDate.getInt("game",0))
    //         .set("human", releaseDate.getString("human",""))
    //         .set("platform", releaseDate.getInt("platform"))
    //         .set("region", releaseDate.getInt("region",0))
    //         .set("lastUpdated", new Date());

    //     UpdateResult result = mongoTemplate.upsert(query, update, "releaseDate");
    //     return result.getMatchedCount()>0 || result.getUpsertedId()!=null;
    // }

    // public boolean deleteReleaseDate(int id) {
    //     Query query = new Query(Criteria.where("_id").is(id));
    //     DeleteResult result = mongoTemplate.remove(query, "releaseDate");
    //     return result.getDeletedCount()>0;
    // }

    // public boolean deleteCollection() {
    //     mongoTemplate.dropCollection("releaseDate");
    //     return true;
    // }
    
}

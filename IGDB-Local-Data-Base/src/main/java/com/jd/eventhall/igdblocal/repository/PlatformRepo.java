package com.jd.eventhall.igdblocal.repository;

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
import proto.Platform;

@Repository
public class PlatformRepo {
    @Autowired
    private MongoTemplate mongoTemplate;

    public boolean collectionExists() {
        return mongoTemplate.collectionExists("platform");
    }

    public boolean insertPlatform(Platform platform) {
        Document doc = new Document("_id", platform.getId())
            .append("name", platform.getName());
        
        Document insertedDoc = mongoTemplate.insert(doc, "platform");
        return insertedDoc.equals(doc);
    }

    public boolean insertPlatform(JsonObject platform) {
        Document doc = new Document("_id", platform.getInt("id"))
            .append("name", platform.getString("name"))
            .append("lastUpdated", new Date());
        
        Document insertedDoc = mongoTemplate.insert(doc, "platform");
        return insertedDoc.equals(doc);
    }

    public long collectionCount() {
        return mongoTemplate.count(new Query(), "platform");
    }

    public boolean updatePlatform(Platform platform) {
        Query query = new Query(Criteria.where("_id").is(platform.getId()));
        Update update = new Update()
            .set("name", platform.getName());
        
        UpdateResult result = mongoTemplate.upsert(query, update, "platform");
        return result.getMatchedCount()>0 || result.getUpsertedId()!=null;
    }

    public boolean updatePlatform(JsonObject platform) {
        Query query = new Query(Criteria.where("_id").is(platform.getInt("id")));
        Update update = new Update()
            .set("name", platform.getString("name"))
            .set("lastUpdated", new Date());
        
        UpdateResult result = mongoTemplate.upsert(query, update, "platform");
        return result.getMatchedCount()>0 || result.getUpsertedId()!=null;
    }

    public boolean deletePlatform(int id) {
        Query query = new Query(Criteria.where("_id").is(id));
        DeleteResult result = mongoTemplate.remove(query, "platform");
        return result.getDeletedCount()>0;
    }

    public boolean deleteCollection() {
        mongoTemplate.dropCollection("platform");
        return true;
    }
}

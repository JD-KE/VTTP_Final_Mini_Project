package com.jd.eventhall.igdblocal.repository;

import java.util.Date;
import java.util.List;

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
import proto.Cover;

@Repository
public class CoverRepo {
    @Autowired
    private MongoTemplate mongoTemplate;

    public String getUrlFromId(long coverId) {
        Query query = new Query(Criteria.where("_id").is(coverId));
        List<Document> docResults = mongoTemplate.find(query, Document.class, "cover");
        List<String> results = docResults.stream()
            .map(doc -> doc.getString("url"))
            .toList();
        if(results.size() == 0) {
            return "";
        }
        return results.getLast();
    }

    public boolean collectionExists() {
        return mongoTemplate.collectionExists("cover");
    }

    public Document insertCover(Cover cover) {
        Document doc = new Document("_id", cover.getId())
            .append("game", cover.getGame().getId())
            .append("image_id", cover.getImageId())
            .append("url", cover.getUrl());
        Document insertedDoc = mongoTemplate.insert(doc, "cover");
        return insertedDoc;
    }

    public boolean insertCover(JsonObject cover) {
        Document doc = new Document("_id", cover.getInt("id"))
            .append("game", cover.getInt("game", 0))
            .append("image_id", cover.getString("image_id",""))
            .append("url", cover.getString("url",""))
            .append("lastUpdated", new Date());
        Document insertedDoc = mongoTemplate.insert(doc, "cover");
        return insertedDoc.equals(doc);
    }

    public long collectionCount() {
        return mongoTemplate.count(new Query(), "cover");
    }

    public boolean updateCover(Cover cover) {
        // Document doc = new Document().toJson();
        Query query = new Query(Criteria.where("_id").is(cover.getId()));
        Update update = new Update()
            .set("game", cover.getGame().getId())
            .set("image_id", cover.getImageId())
            .set("url", cover.getUrl());
        UpdateResult result = mongoTemplate.upsert(query, update, "cover");
        return result.getMatchedCount()>0 || result.getUpsertedId()!=null;
    }

    public boolean updateCover(JsonObject cover) {
        Query query = new Query(Criteria.where("_id").is(cover.getInt("id")));
        Update update = new Update()
            .set("game", cover.getInt("game",0))
            .set("image_id", cover.getString("image_id", ""))
            .set("url", cover.getString("url", ""))
            .set("lastUpdated", new Date());
        UpdateResult result = mongoTemplate.upsert(query, update, "cover");
        return result.getMatchedCount()>0 || result.getUpsertedId()!=null;
    }

    public boolean deleteCover(int id) {
        Query query = new Query(Criteria.where("_id").is(id));
        DeleteResult result = mongoTemplate.remove(query, "cover");
        return result.getDeletedCount()>0;
    }

    public boolean testDeleteCollection() {
        mongoTemplate.dropCollection("cover");
        return true;
    }
    

}

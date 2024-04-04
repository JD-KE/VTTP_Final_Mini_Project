package com.jd.eventhall.MainAppBackend.repository;

import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

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
    
}

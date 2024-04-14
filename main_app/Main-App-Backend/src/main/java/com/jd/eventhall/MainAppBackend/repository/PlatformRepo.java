package com.jd.eventhall.MainAppBackend.repository;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;



@Repository
public class PlatformRepo {
    @Autowired
    private MongoTemplate mongoTemplate;

    public String getPlatforms(List<Integer> platformIds) {
        Query query = new Query(Criteria.where("_id").in(platformIds));
        List<Document> results = mongoTemplate.find(query,
        Document.class,"platform");

        List<String> resultStrings = results.stream()
            .map(doc -> doc.getString("name"))
            .toList();
        

        return String.join(", ", resultStrings);
    }

    
}

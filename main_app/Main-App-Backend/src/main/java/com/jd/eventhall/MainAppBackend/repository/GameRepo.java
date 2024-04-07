package com.jd.eventhall.MainAppBackend.repository;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.stereotype.Repository;


@Repository
public class GameRepo {
    @Autowired
    private MongoTemplate mongoTemplate;
    
    public List<Document> testSearchGamesByName(String searchString, int page, int limit) {
        // Query query = new TextQuery(TextCriteria.forDefaultLanguage()
        //     .matchingPhrase(searchString)
        //     .caseSensitive(false))
        //     .sortByScore()
        //     // .with(Sort.by(Direction.DESC, "rating")
        //     // .and(Sort.by(Direction.DESC,"aggregated_rating")))
        //     .with(Sort.by(
        //         new Sort.Order(Direction.DESC, "rating"),
        //         new Sort.Order(Direction.DESC,"aggregated_rating"),
        //         new Sort.Order(Direction.DESC, "first_release_date")
        //     ))
        //     .skip((page - 1)*limit).limit(limit);
        Query query = new Query()
            .with(Sort.by(
                new Sort.Order(Direction.DESC, "rating"),
                new Sort.Order(Direction.DESC,"aggregated_rating"),
                new Sort.Order(Direction.DESC, "first_release_date")
            ))
            .addCriteria(TextCriteria.forDefaultLanguage()
                .matchingPhrase(searchString)
                .caseSensitive(false)
            ).skip((page - 1)*limit).limit(limit);
        
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

}

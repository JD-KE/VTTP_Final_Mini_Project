package com.jd.eventhall.MainAppBackend.repository;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AddFieldsOperation;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

@Repository
public class ReleaseDateRepo {
    @Autowired
    private MongoTemplate mongoTemplate;
    

    public List<Document> getReleaseDates(List<Integer> releaseDateIds) {


        MatchOperation matchOp = Aggregation.match(Criteria.where("_id")
            .in(releaseDateIds));

        LookupOperation lookupOperation = LookupOperation.newLookup()
            .from("platform")
            .localField("platform")
            .foreignField("_id")
            .as("platform");

        UnwindOperation unwindOp = Aggregation.unwind("platform", true);

        AddFieldsOperation addFieldsOperation = Aggregation
            .addFields()
            .addField("platform")
            .withValue("$platform.name")
            .build();
        SortOperation sortOp = Aggregation.sort(Sort.by(
            new Sort.Order(Direction.DESC, "platform"),
            new Sort.Order(Direction.ASC,"date")
        ));

        Aggregation pipeline = Aggregation.newAggregation(
            matchOp, lookupOperation, unwindOp, addFieldsOperation,sortOp
        );
        List<Document> results = mongoTemplate
            .aggregate(pipeline,"releaseDate", Document.class).getMappedResults();

        return results;
    }
}

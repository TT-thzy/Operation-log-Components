package org.operationlog.manager;

import org.operationlog.domain.MongoCollectionIndex;
import org.operationlog.idGenerate.IdGenerator;
import org.operationlog.mongo.driver.HybridMongoTemplate;
import org.operationlog.mongo.manager.AbstractLongMongoCurdManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MongoCollectionIndexManager extends AbstractLongMongoCurdManager<MongoCollectionIndex, Long> {

    @Autowired
    public MongoCollectionIndexManager(HybridMongoTemplate mongoTemplate, IdGenerator idgenerator) {
        super(mongoTemplate, idgenerator);
    }

    public List<MongoCollectionIndex> findByCollection(String collection) {
        return mongoTemplate.find(Query.query(Criteria.where("collection").is(collection)), MongoCollectionIndex.class);
    }
}

package org.operationlog.service;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.operationlog.domain.MongoCollectionIndex;
import org.operationlog.manager.MongoCollectionIndexManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MongoCollectionIndexService {

    @Autowired
    private MongoCollectionIndexManager mongoCollectionIndexManager;

    private MongoTemplate mongoTemplate;

    public void createCollectionIndex(MongoCollection<Document> collection, String collectionName) {
        List<MongoCollectionIndex> indexes = mongoCollectionIndexManager.findByCollection(collectionName);
        indexes.forEach(index -> {
            BasicDBObject keys = new BasicDBObject();

            index.getKeys().forEach(key -> {
                int direction = key.getDirection() == Sort.Direction.ASC ? 1 : -1;
                keys.append(key.getProperty(), direction);
            });
            collection.createIndex(keys);
        });
    }
}

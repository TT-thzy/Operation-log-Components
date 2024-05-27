package org.operationlog.domain;

import lombok.Data;
import org.operationlog.mongo.domain.LongMongoDomain;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "mongocollectionindexes")
public class MongoCollectionIndex extends LongMongoDomain {

    private String collection;

    private String fieldName;

    private List<MongoCollectionIndexKey> keys;
}

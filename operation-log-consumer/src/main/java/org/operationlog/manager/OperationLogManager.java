package org.operationlog.manager;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.operationlog.domain.OperationLog;
import org.operationlog.idGenerate.IdGenerator;
import org.operationlog.mongo.driver.HybridMongoTemplate;
import org.operationlog.mongo.manager.AbstractLongMongoCurdManager;
import org.operationlog.service.MongoCollectionIndexService;
import org.operationlog.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class OperationLogManager extends AbstractLongMongoCurdManager<OperationLog, Long> {

    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final String collectionName = "operationlogs";

    @Autowired
    private MongoCollectionIndexService mongoCollectionIndexService;

    @Autowired
    public OperationLogManager(HybridMongoTemplate mongoTemplate, IdGenerator idgenerator) {
        super(mongoTemplate, idgenerator);
    }

    @Override
    public OperationLog saveNew(OperationLog operationLog) {
        String subCollectionKey = this.getSubCollectionKey();
        String subCollectionName = collectionName + "_" + subCollectionKey;
        if (!mongoTemplate.collectionExists(subCollectionName)) {
            MongoCollection<Document> collection = mongoTemplate.createCollection(subCollectionName);
            mongoCollectionIndexService.createCollectionIndex(collection, collectionName);
        }
        return saveNew(operationLog, subCollectionName);
    }

    public OperationLog saveNew(OperationLog operationLog, String collection) {
        //如果id不存在
        if (null == operationLog.getId()) {
            operationLog.setId(idgenerator.generateId());
        }
        mongoTemplate.save(operationLog, collection);
        return operationLog;
    }

    /**
     * 获取分表键
     *
     * @return
     */
    private String getSubCollectionKey() {
        List<LocalDate> monDayAndSunday = DateUtils.getMonDayAndSunday(LocalDate.now());

        return monDayAndSunday.get(0).format(dateFormat) + "~" + monDayAndSunday.get(1).format(dateFormat);
    }
}

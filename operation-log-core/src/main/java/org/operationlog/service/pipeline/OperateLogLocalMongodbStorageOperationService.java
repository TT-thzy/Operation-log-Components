package org.operationlog.service.pipeline;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.operationlog.config.pipeline.MongoConfig;
import org.operationlog.domain.OperationLogInfo;
import org.operationlog.utils.JsonUtils;
import org.operationlog.utils.MongodbUtils;

/**
 * @description:
 * @author: TT-Berg
 * @date: 2024/5/27
 **/
public class OperateLogLocalMongodbStorageOperationService implements OperateLogPipelineOperationsService {

    @Override
    public void pipelineOperate(OperationLogInfo operationLogInfo) {
        MongoDatabase mongodbDataBase = this.getMongodbDataBase();

        MongoCollection<Document> collection = mongodbDataBase.getCollection("operationlogs");

        String jsonStr = null;
        try {
            jsonStr = JsonUtils.toJsonStr(operationLogInfo);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Document document = Document.parse(jsonStr);

        collection.insertOne(document);
    }

    private MongoDatabase getMongodbDataBase() {
        MongoConfig mongoConfig = MongodbUtils.getMongoConfig();

        return MongodbUtils.getMongoDatabase(mongoConfig);
    }

    private void closeConnection(MongoDatabase mongoDatabase){
        //todo
    }
}

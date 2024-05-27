package org.operationlog.service;

import org.operationlog.constant.OperationLogPipelineOperationsType;
import org.operationlog.service.pipeline.OperateLogConsoleOperationService;
import org.operationlog.service.pipeline.OperateLogLocalMongodbStorageOperationService;
import org.operationlog.service.pipeline.OperateLogLog4jKafkaAppenderService;
import org.operationlog.service.pipeline.OperateLogPipelineOperationsService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description:
 * @author: TT-Berg
 * @date: 2024/5/24
 **/
public class OperationLogPipelineOperationsFactory {

    private static final Map<String, OperateLogPipelineOperationsService> factory = new ConcurrentHashMap<>();

    static {
        factory.put(OperationLogPipelineOperationsType.CONSOLE, new OperateLogConsoleOperationService());
        factory.put(OperationLogPipelineOperationsType.LOCAL_MONGODB, new OperateLogLocalMongodbStorageOperationService());
        factory.put(OperationLogPipelineOperationsType.LOG4J_KAFKA_APPENDER, new OperateLogLog4jKafkaAppenderService());
    }

    public OperateLogPipelineOperationsService getStorageHandle(String storageType) {
        return factory.get(storageType);
    }

}

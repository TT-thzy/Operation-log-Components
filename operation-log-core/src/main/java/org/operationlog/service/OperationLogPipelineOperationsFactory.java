package org.operationlog.service;

import org.operationlog.constant.OperationLogPipelineOperationsType;
import org.operationlog.service.storage.OperateLogConsoleOperationService;
import org.operationlog.service.storage.OperateLogPipelineOperationsService;

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
    }

    public OperateLogPipelineOperationsService getStorageHandle(String storageType) {
        return factory.get(storageType);
    }

}

package com.operationlog.service;

import com.operationlog.service.storage.OperateLogConsoleService;
import com.operationlog.service.storage.OperateLogStorageService;
import org.operationlog.constant.OperationLogStorageType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description:
 * @author: TT-Berg
 * @date: 2024/5/24
 **/
public class OperationLogStorageFactory {

    private static final Map<String, OperateLogStorageService> factory = new ConcurrentHashMap<>();

    static {
        factory.put(OperationLogStorageType.CONSOLE, new OperateLogConsoleService());
    }

    public OperateLogStorageService getStorageHandle(String storageType) {
        return factory.get(storageType);
    }

}

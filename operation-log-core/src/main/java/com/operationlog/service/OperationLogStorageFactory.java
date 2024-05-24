package com.operationlog.service;

import com.operationlog.service.storage.OperateLogStorageService;

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

    }

    public OperateLogStorageService getStorageHandle(String storageType) {
        return factory.get(storageType);
    }

}

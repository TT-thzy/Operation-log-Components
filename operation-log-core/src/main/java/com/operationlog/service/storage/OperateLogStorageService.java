package com.operationlog.service.storage;

import com.operationlog.domain.OperationLogInfo;

/**
 * @description:
 * @author: TT-Berg
 * @date: 2024/5/24
 **/
public interface OperateLogStorageService {

    void storage(OperationLogInfo operationLogInfo);

}

package org.operationlog.service.storage;

import org.operationlog.domain.OperationLogInfo;

/**
 * @description:
 * @author: TT-Berg
 * @date: 2024/5/24
 **/
public interface OperateLogPipelineOperationsService {

    void pipelineOperate(OperationLogInfo operationLogInfo);

}

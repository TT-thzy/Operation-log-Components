package org.operationlog.service.storage;

import org.operationlog.domain.OperationLogInfo;

/**
 * @description:
 * @author: TT-Berg
 * @date: 2024/5/24
 **/
public class OperateLogConsoleOperationService implements OperateLogPipelineOperationsService {

    @Override
    public void pipelineOperate(OperationLogInfo operationLogInfo) {
        System.out.println(operationLogInfo.toString());
    }

}

package org.operationlog.service.pipeline;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import org.operationlog.domain.OperationLogInfo;
import org.operationlog.utils.JsonUtils;
import org.slf4j.MDC;

import java.util.Objects;

/**
 * @description:
 * @author: TT-Berg
 * @date: 2024/5/27
 **/
@Log4j2
public class OperateLogLog4jKafkaAppenderService implements OperateLogPipelineOperationsService {

    @Override
    public void pipelineOperate(OperationLogInfo operationLogInfo) {
        if (!whetherToOperate()) {
            return;
        }

        //清理上次日志信息（采用的是异步发送kafka）
        MDC.remove("operation");

        // 放入context中
        try {
            MDC.put("operation", JsonUtils.toJsonStr(operationLogInfo));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        log.log(Level.getLevel("OPERATOR"), operationLogInfo.getMessage());
    }

    /**
     * 检测是否可以正常记录操作日志<br />
     * 是否缺少配置文件或者缺少'OPERATOR'的日志级别
     */
    private boolean whetherToOperate() {
        if (Objects.isNull(Level.getLevel("OPERATOR"))) {
            return false;
        }
        return true;
    }
}

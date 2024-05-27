package org.operationlog.constant;

/**
 * @description:
 * @author: TT-Berg
 * @date: 2024/5/24
 **/
public interface OperationLogPipelineOperationsType {

    //本地持久化
    String LOCAL = "LOCAL";

    //http传输至日志服务
    String HTTP = "HTTP";

    //kafka传输至日志服务
    String KAFKA = "KAFKA";

    //log4j's kafka appender传输至日志服务
    String LOG4J_KAFKA_APPENDER = "LOG4J_KAFKA_APPENDER";

    //控制台输出
    String CONSOLE = "CONSOLE";
}

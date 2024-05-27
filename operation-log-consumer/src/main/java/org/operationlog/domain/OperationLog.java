package org.operationlog.domain;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import org.operationlog.enumeration.CodeVariableType;
import org.operationlog.mongo.domain.LongMongoDomain;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Map;

@Data
@Document("operationlogs")
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
public class OperationLog extends LongMongoDomain {

    /**
     * 参数信息
     */
    private Map<String, Object> params;

    /**
     * 执行结果
     */
    private Object result;

    /**
     * 操作人
     */
    private Long operateUser;

    /**
     * 调用时间
     */
    private Date time;

    /**
     * 方法执行时间
     */
    private Long duration;

    /**
     * 操作日志级别
     */
    private String level;

    /**
     * 标签
     */
    private Map<String, String> tags;

    /**
     * 日志信息
     */
    private String message;

    /**
     * 模块
     */
    private String module;

    /**
     * 操作类型
     */
    private String operator;

    /**
     * 调用者ip
     */
    private String ip;

    /**
     * 应用名称
     */
    private String applicationName;

    /**
     * 请求信息
     */
    private Map<String, Object> requestInfo;

    /**
     * 打印日志的代码信息
     * CodeVariableType 日志记录的ClassName、MethodName
     */
    private Map<CodeVariableType, Object> codeVariable;

    /**
     * 方法是否执行成功
     */
    private boolean success;
}

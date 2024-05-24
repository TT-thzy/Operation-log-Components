package com.operationlog.service;

import com.operationlog.OperationLog;
import com.operationlog.OperationLogTag;
import com.operationlog.domain.MethodExecuteResult;
import com.operationlog.domain.OperationLogInfo;
import org.operationlog.enumeration.CodeVariableType;
import org.apache.commons.lang3.StringUtils;
import org.operationlog.utils.FreemarkerUtils;
import org.operationlog.utils.JsonUtils;
import org.slf4j.MDC;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @description:
 * @author: TT-Berg
 * @date: 2024/5/24
 **/
public class OperationLogRecorder {

    private OperationLogStorageFactory factory;

    {
        factory = new OperationLogStorageFactory();
    }

    public void recordOperationLog(MethodExecuteResult methodExecuteResult, OperationLog operationLog, String module,
                                   Map<String, Object> params, String applicationName, String storageType) throws IOException {
        //获取操作信息
        String operateMessage = this.getOperationMessage(methodExecuteResult, params, module, operationLog);

        // 获取tags
        Map<String, String> tags = getTags(operationLog, params, methodExecuteResult, module);

        Map<CodeVariableType, Object> codeVariable = this.getCodeVariable(methodExecuteResult.getMethod());

        // 创建操作日志信息
        OperationLogInfo operationLogInfo = new OperationLogInfo(params, methodExecuteResult.getResult(), applicationName,
                methodExecuteResult.getRequest(), getIpAddress(methodExecuteResult.getRequest()), methodExecuteResult.getOperateUser(),
                methodExecuteResult.getStart(), methodExecuteResult.getEnd(), operationLog, tags, operateMessage, module,
                codeVariable, methodExecuteResult.isSuccess());

        // 存储操作日志
        factory.getStorageHandle(storageType).storage(operationLogInfo);
    }

    private String getOperationMessage(MethodExecuteResult methodExecuteResult, Map<String, Object> params,
                                       String module, OperationLog operationLog) throws IOException {
        String operateMessage = null;
        if (methodExecuteResult.isSuccess()) {
            Object result = methodExecuteResult.getResult();
            String success = operationLog.success();
            if (StringUtils.isBlank(success)) {
                return null;
            }
            operateMessage = FreemarkerUtils.formatString(success, params, result, module, null);
        } else {
            String errorMsg = methodExecuteResult.getErrorMsg();
            String fail = operationLog.fail();
            if (StringUtils.isBlank(fail)) {
                return null;
            }
            operateMessage = FreemarkerUtils.formatString(fail, params, null, module, errorMsg);
        }
        return operateMessage;
    }

    /**
     * 获取tags
     */
    private Map<String, String> getTags(OperationLog operationLog, Map<String, Object> params,
                                        MethodExecuteResult methodExecuteResult, String module) throws IOException {
        Object result = methodExecuteResult.getResult();
        String errorMsg = methodExecuteResult.getErrorMsg();
        String json = MDC.getCopyOfContextMap().get("tags");
        Map<String, String> tags;
        if (Objects.nonNull(json)) {
            tags = JsonUtils.readValue(json, Map.class);
        } else {
            tags = new HashMap<>();
        }
        for (OperationLogTag tag : operationLog.tags()) {
            String value = FreemarkerUtils.formatString(tag.value(), params, result, module, errorMsg);
            tags.put(tag.key(), value);
        }
        return tags;
    }

    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        if (StringUtils.isNotEmpty(ip) && !"unKnow".equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            return ip;
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    private Map<CodeVariableType, Object> getCodeVariable(Method method) {
        Map<CodeVariableType, Object> map = new HashMap<>();
        map.put(CodeVariableType.ClassName, method.getDeclaringClass());
        map.put(CodeVariableType.MethodName, method.getName());
        return map;
    }
}

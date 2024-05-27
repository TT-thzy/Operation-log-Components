package org.operationlog.domain;

import org.operationlog.OperationLog;
import lombok.ToString;
import org.operationlog.enumeration.CodeVariableType;
import lombok.Data;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 操作日志信息
 *
 * @author Michael
 * @date 2018/11/05
 */
@Data
@ToString
public class OperationLogInfo {

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

    public OperationLogInfo(Map<String, Object> params, Object result, String applicationName, HttpServletRequest request,
                            String ip, Long operateUser, Instant start, Instant end, OperationLog operationLog,
                            Map<String, String> tags, String message, String module, Map<CodeVariableType, Object> codeVariable, boolean success) {
        this.params = params;
        this.result = result;
        this.operateUser = operateUser;
        this.time = Date.from(start);
        this.duration = Duration.between(start, end).toMillis();
        this.level = operationLog.level().name();
        this.tags = tags;
        this.message = message;
        this.module = module;
        this.operator = operationLog.operator().name();
        this.ip = ip;
        this.applicationName = applicationName;
        Map<String, Object> requestInfo = new HashMap<>();
        requestInfo.put("url", request.getRequestURL());
        requestInfo.put("method", request.getMethod());
        requestInfo.put("headers", this.buildHeaders(request));
        if (request instanceof ContentCachingRequestWrapper) {
            requestInfo.put("body", new String(((ContentCachingRequestWrapper) request).getContentAsByteArray()));
        }
        this.requestInfo = requestInfo;
        this.codeVariable = codeVariable;
        this.success = success;
    }

    private Map<String, Object> buildHeaders(HttpServletRequest request) {
        Map<String, Object> headers = new HashMap<>();
        final Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            final String s = headerNames.nextElement();
            final String header = request.getHeader(s);
            headers.put(s, header);
        }
        return headers;
    }
}

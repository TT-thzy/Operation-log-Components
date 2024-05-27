package org.operationlog.domain;

import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.Instant;

/**
 * @description:
 * @author: TT-Berg
 * @date: 2024/5/24
 **/
@Data
public class MethodExecuteResult {

    private boolean success;
    private Throwable throwable;
    private String errorMsg;
    private Object result;
    private final Method method;
    private final Object[] args;
    private final Class<?> targetClass;
    private Long operateUser;
    private HttpServletRequest request;
    private Instant start;
    private Instant end;

    public MethodExecuteResult(Method method, Object[] args, Class<?> targetClass, Long operateUser, HttpServletRequest request) {
        this.method = method;
        this.args = args;
        this.targetClass = targetClass;
        this.operateUser = operateUser;
        this.request = request;
    }
}

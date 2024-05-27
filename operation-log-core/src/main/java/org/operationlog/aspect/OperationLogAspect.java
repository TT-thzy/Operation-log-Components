package org.operationlog.aspect;

import com.google.common.collect.ImmutableList;
import org.operationlog.OperationLog;
import org.operationlog.OperationLogModule;
import org.operationlog.domain.MethodExecuteResult;
import org.operationlog.service.OperationLogRecorder;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.operationlog.aware.UserGetterAware;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.codec.multipart.Part;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.lang.reflect.Method;
import java.security.Principal;
import java.time.Instant;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.IntStream;

@Aspect
public class OperationLogAspect implements UserGetterAware {

    private static List<Class<?>> ignoreType;
    static {
        ignoreType = ImmutableList.of(ServletRequest.class, HttpServletRequest.class, MultipartRequest.class,
                MultipartHttpServletRequest.class, ServletResponse.class, HttpServletResponse.class,
                HttpSession.class, WebRequest.class, NativeWebRequest.class, InputStream.class, Reader.class,
                OutputStream.class, Writer.class, Principal.class, MultipartFile.class, Part.class, Errors.class,
                BindingResult.class, SessionStatus.class, UriComponentsBuilder.class, HttpEntity.class
        );
    }

    // 操作人hook
    private Supplier<Optional<Long>> userGetter = Optional::empty;

    // 应用名称
    private String applicationName;

    // 日志持久化方式
    private String storageType;

    private OperationLogRecorder operationLogRecorder;

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(OperationLogAspect.class);

    // 操作日志状态
    private static boolean enable = true;

    public void enable() {
        enable = true;
    }

    public void disable() {
        enable = false;
    }

    public OperationLogAspect(String applicationName, String storageType, OperationLogRecorder operationLogRecorder) {
        this.applicationName = applicationName;
        this.storageType = storageType;
        this.operationLogRecorder = operationLogRecorder;
    }

    private OperationLogAspect() {

    }

    @Around(value = "@annotation(org.operationlog.OperationLog)")
    public Object invokeMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        // get args
        Object[] arguments = joinPoint.getArgs();
        //get method
        Method method = this.getMethod(joinPoint);
        /*
          以下情况直接运行, 不记录操作日志
          <1>非Web应用
          <2>enable == false(不启用操作日志)
         */
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        if (Objects.isNull(requestAttributes) || Objects.isNull(requestAttributes.getRequest()) || !enable) {
            return joinPoint.proceed(arguments);
        }

        MethodExecuteResult methodExecuteResult = new MethodExecuteResult(method, arguments, method.getDeclaringClass(),
                userGetter.get().orElse(null), request);

        // 记录开始时间
        Instant start = Instant.now();
        Object result = null;
        try {
            result = joinPoint.proceed(arguments);
            methodExecuteResult.setSuccess(true);
            methodExecuteResult.setResult(result);
        } catch (Exception ex) {
            methodExecuteResult.setSuccess(false);
            methodExecuteResult.setThrowable(ex);
            methodExecuteResult.setErrorMsg(ex.getMessage());
        }
        // 记录结束时间
        Instant end = Instant.now();
        methodExecuteResult.setStart(start);
        methodExecuteResult.setEnd(end);

        try {
            // 获取切点信息
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            // 获取@OperationLog注解的信息
            OperationLog operationLog = methodSignature.getMethod().getAnnotation(OperationLog.class);
            //获取模块信息
            String module = getModule(operationLog, joinPoint);
            // 当没有获取到module时, 不记录操作日志
            if (StringUtils.isEmpty(module)) {
                return result;
            }
            // 获取参数
            String[] parameterNames = methodSignature.getParameterNames();
            Map<String, Object> params = this.generateParams(parameterNames, arguments);
            operationLogRecorder.recordOperationLog(methodExecuteResult, operationLog, module, params, applicationName, storageType);
        } catch (Exception ex) {
            LOGGER.error("generate operation log fail", ex);
        }

        if (null != methodExecuteResult.getThrowable()) {
            throw methodExecuteResult.getThrowable();
        }

        return result;
    }

    private Method getMethod(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        return methodSignature.getMethod();
    }

    /**
     * 获取module<br />
     * 先从方法注解@OperationLog上获取<br />
     * 如果@OperationLog上没有, 则从类@OperationLogModule注解上获取<br />
     * 都没有则返回空
     */
    private String getModule(OperationLog operationLog, ProceedingJoinPoint joinPoint) {
        String module;
        module = operationLog.module();
        if (StringUtils.isEmpty(module)) {
            OperationLogModule moduleAnnotation = joinPoint.getTarget().getClass().getAnnotation(OperationLogModule.class);
            if (Objects.isNull(moduleAnnotation)) {
                return "";
            }
            return moduleAnnotation.value();
        }
        return module;
    }

    /**
     * 生成controller接收大到参数信息
     *
     * @param parameterNames
     * @param args
     * @return
     */
    private Map<String, Object> generateParams(String[] parameterNames, Object[] args) {
        Map<String, Object> params = new HashMap<>();
        if (parameterNames.length > 0) {

            IntStream.range(0, parameterNames.length).filter(i -> !isIgnoreParam(args[i]))
                    .forEach(i -> params.put(parameterNames[i], args[i]));
        }

        return params;
    }

    /**
     * 判断参数是否应该忽略
     */
    private boolean isIgnoreParam(Object arg) {
        return Objects.nonNull(arg) && ignoreType.stream().anyMatch(type -> type.isAssignableFrom(arg.getClass()));
    }

    @Override
    public void setUserGetter(Supplier<Optional<Long>> userGetter) {
        this.userGetter = userGetter;
    }
}

package com.operationlog;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于指定操作日志的module<br />
 * 当方法上已经使用@OperationLog的module属性指定module时, 优先使用@OperationLog的module属性
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface OperationLogModule {

    String value();
}

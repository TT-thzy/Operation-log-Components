package com.operationlog;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OperationLog {

    Level level() default Level.TRIVIAL;

    String success() default "";

    String fail() default "";

    OperationLogTag[] tags() default {};

    String module() default "";

    Operation operator();

    enum Level {
        TRIVIAL,
        WARN,
        IMPORTANT
    }

    enum Operation {
        CREATE,
        DELETE,
        UPDATE,
        GET
    }
}

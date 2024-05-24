package org.operationlog.config;

import org.operationlog.aware.UserGetterBeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @author: TT-Berg
 * @date: 2024/5/24
 **/
@Configuration
public class SecurityConfig {

    @Configuration
    @ConditionalOnClass(UserGetterBeanPostProcessor.class)
    public static class UserGetterBeanPostProcessorConfiguration {

        /**
         * Spring后置处理器<br/>
         * 给继承了UserGetterAware接口的Bean添加获取当前登录用户的方法
         */
        @ConditionalOnMissingBean
        @Bean
        public UserGetterBeanPostProcessor userGetterBeanPostProcessor() {
            return new UserGetterBeanPostProcessor();
        }

    }
}

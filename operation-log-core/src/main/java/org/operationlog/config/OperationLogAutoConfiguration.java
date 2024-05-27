package org.operationlog.config;

import org.operationlog.aspect.OperationLogAspect;
import org.operationlog.service.OperationLogRecorder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(OperationLogProperties.class)
@ConditionalOnClass(OperationLogAspect.class)
public class OperationLogAutoConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "operation-log", name = "enable", havingValue = "true", matchIfMissing = true)
    @ConditionalOnMissingBean(OperationLogAspect.class)
    public OperationLogAspect operationLogAspect(OperationLogProperties properties) {
        return new OperationLogAspect(properties.getApplicationName(), properties.getStorageType(), this.operationLogRecorder());
    }

    @Bean
    @ConditionalOnMissingBean(OperationLogRecorder.class)
    public OperationLogRecorder operationLogRecorder() {
        return new OperationLogRecorder();
    }
}

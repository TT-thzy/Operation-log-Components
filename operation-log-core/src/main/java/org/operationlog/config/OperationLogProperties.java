package org.operationlog.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("operation-log")
@Getter
@Setter
public class OperationLogProperties {

    private String applicationName;

    private String storageType;
}

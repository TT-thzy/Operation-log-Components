package org.operationlog.utils;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.apache.commons.lang3.StringUtils;
import org.operationlog.config.pipeline.MongoConfig;
import org.operationlog.context.SpringApplicationContext;
import org.springframework.boot.autoconfigure.mongo.MongoClientFactory;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.autoconfigure.mongo.MongoPropertiesClientSettingsBuilderCustomizer;
import org.springframework.core.env.Environment;

import java.util.Arrays;

/**
 * @description:
 * @author: TT-Berg
 * @date: 2024/5/27
 **/
public class MongodbUtils {

    private final static String DEFAULT_MONGODB_CONNECTION_URI = "mongodb://localhost:27017/operation-logs";

    public static MongoDatabase getMongoDatabase(MongoConfig mongoConfig) {
        MongoProperties properties = new MongoProperties();
        if (StringUtils.isNotBlank(mongoConfig.getUri())) {
            properties.setUri(mongoConfig.getUri());
        } else {
            String host = mongoConfig.getHost();
            int port = mongoConfig.getPort();
            String userName = mongoConfig.getUsername();
            String password = mongoConfig.getPassword();

            properties.setHost(host);
            properties.setPort(port);
            properties.setUsername(userName);
            properties.setPassword(password.toCharArray());
            properties.setDatabase(mongoConfig.getDatabase());
        }

        MongoPropertiesClientSettingsBuilderCustomizer builderCustomizer =
                new MongoPropertiesClientSettingsBuilderCustomizer(properties, SpringApplicationContext.getBeanStatic(Environment.class));
        MongoClientSettings settings = MongoClientSettings.builder().build();
        MongoClient mongoClient = new MongoClientFactory(Arrays.asList(builderCustomizer)).createMongoClient(settings);
        MongoDatabase mongoDatabase = mongoClient.getDatabase(properties.getMongoClientDatabase());
        return mongoDatabase;
    }


    public static MongoConfig getMongoConfig() {
        String uri = getUri();

        if (StringUtils.isBlank(uri)) {
            throw new RuntimeException("获取目标数据库失败，未找到指定的Mongo连接地址");
        }

        MongoConfig mongoConfig = new MongoConfig(uri);
        return mongoConfig;
    }

    public static String getUri() {
        String mongodbUri = SpringApplicationContext.getApplicationContextStatic()
                .getEnvironment()
                .getProperty("spring.data.mongodb.uri");

        if (StringUtils.isBlank(mongodbUri)) {
            mongodbUri = DEFAULT_MONGODB_CONNECTION_URI;
        }

        return mongodbUri;
    }
}

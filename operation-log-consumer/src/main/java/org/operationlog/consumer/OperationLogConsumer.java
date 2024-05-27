package org.operationlog.consumer;

import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.operationlog.domain.OperationLog;
import org.operationlog.manager.OperationLogManager;
import org.operationlog.utils.JsonUtils;
import org.operationlog.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

@Log4j2
@Component
public class OperationLogConsumer implements Consumer<Message<String>> {

    @Autowired
    private OperationLogManager operationLogManager;

    @Override
    public void accept(Message<java.lang.String> message) {
        ConsumerRecord<String, String> data = MessageUtils.convertToSingleRecord(message);
        Acknowledgment acknowledgment = message.getHeaders().get(KafkaHeaders.ACKNOWLEDGMENT, Acknowledgment.class);
        String groupId = message.getHeaders().get(KafkaHeaders.GROUP_ID, String.class);

        log.debug("operation log server receive a message, topic:{}, offset:{}, partition:{}, group: {}",
                data.topic(), data.offset(), data.partition(), groupId);

        String json = handleLog4jStreamJsonData(data.value());

        OperationLog operationLog = null;
        try {
            Map<String, String> map = JsonUtils.readValue(json, Map.class);
            String operationLogJson = map.get("operation");
            operationLog = JsonUtils.readValue(operationLogJson, OperationLog.class);
        } catch (IOException e) {
            log.error("topic operation-logs offset: " + data.offset() + ", data: " + data.value() + " is not a valid OperationLog:", e);
        }

        if (Objects.nonNull(operationLog)) {
            log.debug("data: {applicationName: {}, module: {}, operator: {}, message: {}}",
                    operationLog.getApplicationName(), operationLog.getModule(), operationLog.getOperator(), operationLog.getMessage());

            operationLogManager.saveNew(operationLog);
        }

        if (acknowledgment != null) {
            acknowledgment.acknowledge();
        }
    }

    /**
     * 修复Log4j发送的LogEvent的json数据以","开头
     *
     * @param json
     * @return
     */
    private String handleLog4jStreamJsonData(String json) {
        json = json.trim();
        if (json.startsWith(",")) {
            json = json.replaceFirst(",", "");
        }
        return json;
    }
}

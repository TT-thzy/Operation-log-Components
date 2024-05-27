package org.operationlog.utils;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

/**
 * @description:
 * @author: TT-Berg
 * @date: 2024/5/27
 **/
public class MessageUtils {

    public static <T> ConsumerRecord<String, T> convertToSingleRecord(Message<T> message) {

        MessageHeaders headers = message.getHeaders();

        String topic = headers.get(KafkaHeaders.RECEIVED_TOPIC, String.class);
        Integer partition = headers.get(KafkaHeaders.RECEIVED_PARTITION_ID, Integer.class);
        Long offset = headers.get(KafkaHeaders.OFFSET, Long.class);

        ConsumerRecord<String, T> record = new ConsumerRecord<>(topic, partition, offset, null, message.getPayload());
        return record;
    }
}

package kapyrin.myshopspring.service.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaConsumer {
    @KafkaListener(topics = {"order-topic", "product-topic", "user-topic"}, groupId = "shop-group")
    public void consumeMessages(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
        log.info("Received message: {}", record.value());
        log.warn("From topic: {}", record.topic());
        log.warn("From partition: {}", record.partition());
        log.warn("From offset: {}", record.offset());
        acknowledgment.acknowledge();
    }
}




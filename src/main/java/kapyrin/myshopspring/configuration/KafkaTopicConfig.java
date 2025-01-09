package kapyrin.myshopspring.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {
    @Bean
    public NewTopic orderTopic() {
        return new NewTopic("order-topic", 2, (short) 1);
    }

    @Bean
    public NewTopic userTopic() {
        return new NewTopic("user-topic", 2, (short) 1);
    }

    @Bean
    public NewTopic productTopic() {
        return new NewTopic("product-topic", 2, (short) 1);
    }
}

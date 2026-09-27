package app.Producer.UserEvents;

import app.Configs.Kafka.KafkaAppConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserCreatedProducer {
    private final KafkaTemplate<String, Object> template;

    public void publish(String key, Object value) {
        this.template.send(
                        KafkaAppConfig.Topics.USER_CREATED,
                        key,
                        value
                )
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish event for order {}", key, ex);
                        // handle: retry, DLQ, alert
                    } else {
                        log.info("Published to partition {} offset {}",
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    }
                });
    }
}

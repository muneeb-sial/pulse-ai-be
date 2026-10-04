package app.consumer.userevents;


import app.configs.kafka.KafkaAppConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserCreatedEmailSenderConsumer {

    @KafkaListener(
            topics = KafkaAppConfig.Topics.USER_CREATED,
            groupId = KafkaAppConfig.consumerGroups.EMAIL_CONSUMER,
            concurrency = "1"
    )
    public void consume(Object event, Acknowledgment ack) {
        try {
            log.info("Received OrderEvent");

            // your actual business logic here
            process(event);

            ack.acknowledge(); // only commit offset after successful processing
        } catch (Exception e) {
            log.error("Failed to process OrderEvent", e);
            // don't ack — message will be redelivered, or route to DLQ (see error handler)
        }
    }

    private void process(Object event) {
        // e.g. update order status in DB, send notification, trigger downstream workflow
    }

}

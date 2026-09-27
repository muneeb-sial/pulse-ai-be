package app.Configs.Kafka;

public class KafkaAppConfig {
    //    <domain>.<event>.<event-type>
    public static class Topics {
        public static final String USER_CREATED = "user.created";
    }

    public static class consumerGroups {
        public static final String EMAIL_CONSUMER = "email-service";
    }
}

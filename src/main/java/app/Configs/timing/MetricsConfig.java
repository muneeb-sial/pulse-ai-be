package app.configs.timing;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Makes Micrometer's {@code @Timed} usable on any Spring bean method.
 *
 * Spring Boot does not register {@link TimedAspect} itself, so without this bean
 * {@code @Timed} is silently ignored - the annotation compiles and does nothing.
 *
 * {@code @Timed} is the metrics counterpart to {@link LogExecutionTime}: the latter
 * writes one line per call to the log, the former feeds a timer that Prometheus
 * scrapes, so percentiles survive past the call itself.
 *
 * <pre>
 * &#64;Timed(value = "search.suggest", description = "autocomplete lookups")
 * public List&lt;String&gt; suggest(String q) { ... }
 * </pre>
 *
 * Same proxy limits as {@link LogExecutionTime}: Spring beans, public methods,
 * and only on calls arriving from another bean.
 */
@Configuration
public class MetricsConfig {

    @Bean
    public TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }
}

package app.configs.timing;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Logs how long a method takes, in ms. On a class it times every public method of that bean;
 * a method-level annotation overrides the class-level one.
 * <pre>
 * &#64;LogExecutionTime                                  // SearchService.suggest took 12 ms
 * &#64;LogExecutionTime(logArgs = true, logResult = true) // ... args=[jo, null, true, 8] result=[...]
 * </pre>
 * Applied through a Spring proxy (see {@link LogExecutionTimeAspect}), so it only fires on Spring beans,
 * on public methods, and on calls coming from another bean - {@code this.foo()} is not timed.
 * On a controller it times the handler method, not the filter chain (auth, serialization) around it.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogExecutionTime {

    /** Log the arguments. Off by default: they may hold passwords, tokens or patient data. */
    boolean logArgs() default false;

    /**
     * Log the return value. Off by default: big lists are noisy, and {@code toString()} on a JPA
     * entity can trigger lazy loading.
     */
    boolean logResult() default false;
}

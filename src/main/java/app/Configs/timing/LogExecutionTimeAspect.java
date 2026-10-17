package app.configs.timing;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LogExecutionTimeAspect {

    /** Longer args / results are cut off so a large payload can't flood the log. */
    private static final int MAX_VALUE_LENGTH = 500;

    @Around("@annotation(app.configs.timing.LogExecutionTime) || @within(app.configs.timing.LogExecutionTime)")
    public Object time(ProceedingJoinPoint pjp) throws Throwable {
        LogExecutionTime config = resolve(pjp);
        String name = pjp.getSignature().getDeclaringType().getSimpleName() + "." + pjp.getSignature().getName();

        long start = System.nanoTime();
        try {
            Object result = pjp.proceed();
            long ms = (System.nanoTime() - start) / 1_000_000;

            StringBuilder line = new StringBuilder(name).append(" took ").append(ms).append(" ms");
            if (config.logArgs()) line.append(" args=").append(truncate(Arrays.deepToString(pjp.getArgs())));
            if (config.logResult()) line.append(" result=").append(truncate(String.valueOf(result)));
            log.info(line.toString());

            return result;
        } catch (Throwable ex) {
            long ms = (System.nanoTime() - start) / 1_000_000;
            String args = config.logArgs() ? " args=" + truncate(Arrays.deepToString(pjp.getArgs())) : "";
            log.warn("{} failed after {} ms ({}){}", name, ms, ex.getClass().getSimpleName(), args);
            throw ex;
        }
    }

    /** Method-level annotation wins over class-level. */
    private LogExecutionTime resolve(ProceedingJoinPoint pjp) {
        Object target = pjp.getTarget();
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        Class<?> targetClass = target != null ? AopUtils.getTargetClass(target) : method.getDeclaringClass();

        LogExecutionTime onMethod = AnnotatedElementUtils.findMergedAnnotation(
                AopUtils.getMostSpecificMethod(method, targetClass), LogExecutionTime.class);
        if (onMethod != null) return onMethod;
        return AnnotatedElementUtils.findMergedAnnotation(targetClass, LogExecutionTime.class);
    }

    private static String truncate(String value) {
        return value.length() <= MAX_VALUE_LENGTH ? value : value.substring(0, MAX_VALUE_LENGTH) + "...(truncated)";
    }
}

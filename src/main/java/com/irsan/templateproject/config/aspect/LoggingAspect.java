package com.irsan.templateproject.config.aspect;

import com.irsan.templateproject.utility.annotation.Logging;
import com.irsan.templateproject.utility.util.LoggerUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 * @author : Irsan Ramadhan
 * @email : irsan.ramadhan@iconpln.co.id
 * @date : 27/05/2024
 */
@Aspect
@Component
public class LoggingAspect {

    private final LoggerUtil log = new LoggerUtil(LoggingAspect.class);
    private final ThreadLocal<String> requestId = new ThreadLocal<>();

    @Around("@annotation(logging)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint, Logging logging) throws Throwable {
        String name = logging.name();
        if (name.isEmpty()) {
            name = joinPoint.getSignature().getName();
        }

        if (logging.traceId()) {
            requestId.set(LoggerUtil.generateTraceId());
            MDC.put("requestId", requestId.get());
        }

        StopWatch watch = new StopWatch();
        watch.start();
        log.info("## {} is executing", name);
        Object proceed = joinPoint.proceed();
        watch.stop();
        log.info("## {} executed in {} ms", name, watch.getTotalTimeMillis());
        return proceed;
    }

}

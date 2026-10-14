package kz.iitu.springlab.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

// individual assignment, variant 2: slow-method log
@Aspect
@Component
@Order(4)
public class SlowMethodAspect {

    private static final Logger log =
            LoggerFactory.getLogger(SlowMethodAspect.class);

    public record SlowCall(String method, long millis, long thresholdMs, String at) { }

    private final List<SlowCall> slowCalls = new CopyOnWriteArrayList<>();

    @Value("${app.aspect.slow-threshold-ms}")
    private long thresholdMs;

    @Around("kz.iitu.springlab.aspect.Pointcuts.serviceOperation()")
    public Object collectSlow(ProceedingJoinPoint pjp) throws Throwable {
        long started = System.nanoTime();
        try {
            return pjp.proceed();
        } finally {
            long ms = (System.nanoTime() - started) / 1_000_000;
            if (ms > thresholdMs) {
                SlowCall call = new SlowCall(pjp.getSignature().toShortString(), ms,
                                             thresholdMs, LocalDateTime.now().toString());
                slowCalls.add(call);
                log.warn("[SLOW] {} took {} ms (threshold {} ms), collected: {}",
                         call.method(), ms, thresholdMs, slowCalls.size());
            }
        }
    }

    public List<SlowCall> getSlowCalls() {
        return List.copyOf(slowCalls);
    }

    public long getThresholdMs() {
        return thresholdMs;
    }
}

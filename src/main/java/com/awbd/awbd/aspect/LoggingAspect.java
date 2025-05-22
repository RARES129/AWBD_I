package com.awbd.awbd.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Slf4j
@Component
public class LoggingAspect {

    @Around("execution(* com.awbd.awbd.service..*(..))")
    public Object logAroundMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();

        log.info(">> Entering method: {}", methodName);
        log.info("   Arguments: {}", Arrays.toString(args));

        long start = System.currentTimeMillis();

        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - start;

            log.info("<< Exiting method: {}", methodName);
            log.info("   Return value: {}", result);
            log.info("   Execution time: {} ms", duration);

            return result;

        } catch (Throwable throwable) {
            long duration = System.currentTimeMillis() - start;
            log.error("!! Exception in method: {}", methodName);
            log.error("   Message: {}", throwable.getMessage());
            log.error("   Execution time before failure: {} ms", duration);
            throw throwable;
        }
    }
}

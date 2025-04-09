package com.mvntest.sumerge.common;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Aspect
@Configuration
public class LoggerInterceptor {

    @Pointcut("within(@org.springframework.stereotype.Repository *)" +
            " || within(@org.springframework.stereotype.Service *)" +
            " || within(@org.springframework.web.bind.annotation.RestController *)")
    public void springBeanPointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }

    @Around("springBeanPointcut()")
    public Object auditMethod(ProceedingJoinPoint jp) throws Throwable {
        String methodName = jp.getSignature().getName();
        Logger logger = LoggerFactory.getLogger(jp.getSignature().getDeclaringType());
        logger.info("Call to {} with:{}", methodName, Arrays.toString(jp.getArgs()));
        try {
            Object obj = jp.proceed();
            logger.debug("Method called successfully: {}", methodName);
            return obj;
        } catch (Exception ex) {
            logger.error("Exception: {}", ex.getMessage());
            throw ex;
        }
    }
}

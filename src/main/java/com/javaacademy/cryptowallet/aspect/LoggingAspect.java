package com.javaacademy.cryptowallet.aspect;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

@Component
@Aspect
@Slf4j
public class LoggingAspect {
    @Pointcut("execution(public * com.javaacademy.cryptowallet.controller.*.*(..))")
    public void logController() { }

    @Pointcut("execution(public * com.javaacademy.cryptowallet.service.*.*(..))")
    public void logService() { }

    @Before("logController()")
    public void doBeforeController(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = null;
        if (attributes != null) {
            request = attributes.getRequest();
        }
        if (request != null) {
            log.info("NEW REQUEST: IP: {}, URL: {}, HTTP METHOD: {}, CONTROLLER METHOD: {}.{}",
                    request.getRemoteAddr(),
                    request.getRequestURL().toString(),
                    request.getMethod(),
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName());
        }
    }

    @Before("logService()")
    public void doBeforeService(JoinPoint joinPoint) {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();

        Object[] args = joinPoint.getArgs();
        String argsString = args.length > 0 ? Arrays.toString(args) : "METHOD HAS NO ARGUMENTS";
        log.info("RUN SERVICE: SERVICE METHOD: {}.{}\nMETHOD ARGUMENTS: [{}]",
                className, methodName, argsString);
    }

    @AfterReturning(returning = "returnObject", pointcut = "logController()")
    public void doAfterReturning(Object returnObject) {
        log.info("Return value: {}", returnObject);
    }

    @AfterThrowing(throwing = "ex", pointcut = "logController()")
    public void throwsException(JoinPoint joinPoint, Exception exception) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        log.error("Exception in: {}.{} with arguments {}. Exception message: {}",
                className, methodName, Arrays.toString(joinPoint.getArgs()), exception.getMessage());
    }
}

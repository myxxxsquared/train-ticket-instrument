package travel2.repository;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Component;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Aspect
@Component
public class RepositoryLoggingAspect {

    private static final Logger logger = LogManager.getLogger(RepositoryLoggingAspect.class);

    public RepositoryLoggingAspect() {
        logger.info("RepositoryLoggingAspect initialized.");
    }

    @Before("execution(* travel2.repository.*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        logger.info("Before method: " + joinPoint.getSignature().getName());
    }

    @AfterReturning(pointcut = "execution(* travel2.repository.*.*(..))", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        logger.info("After method: " + joinPoint.getSignature().getName());
        logger.info("Result: " + result);
    }
}

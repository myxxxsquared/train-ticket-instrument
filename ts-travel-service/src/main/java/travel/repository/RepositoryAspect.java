package travel.repository;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import java.util.Arrays;
import java.util.Enumeration;
import javax.persistence.NonUniqueResultException;

@Component
@Aspect
public class RepositoryAspect {

    private final Logger logger = LogManager.getLogger(RepositoryAspect.class);

    @Pointcut("execution(* travel.repository.TripRepository.*(..))")
    public void repositoryMethods() {}

    @Around("repositoryMethods()")
    public Object logRepositoryAccess(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.info("Before executing method: {}", joinPoint.getSignature().toShortString());
        try {
            Object result = joinPoint.proceed();
            logger.info("Successfully executed method: {}", joinPoint.getSignature().toShortString());
            return result;
        } catch (Exception e) {
            logger.error("Error executing method: {}", joinPoint.getSignature().toShortString(), e);
            throw e;
        }
    }
}

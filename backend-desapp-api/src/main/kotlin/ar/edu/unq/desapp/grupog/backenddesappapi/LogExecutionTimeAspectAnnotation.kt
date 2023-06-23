package ar.edu.unq.desapp.grupog.backenddesappapi

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Aspect
@Component
@Order(1)
class LogExecutionTimeAspectAnnotation {

    var logger: Logger = LoggerFactory.getLogger(LogExecutionTimeAspectAnnotation::class.java)

    /// ANNOTATION POINTCUT////
    @Around("@annotation(LogExecutionTime)")
    @Throws(Throwable::class)
    fun logExecutionTimeAnnotation(joinPoint: ProceedingJoinPoint): Any? {
        logger.info("/////// LogExecutionTimeAspectAnnotation - AROUND START  logExecutionTime annotation //////")
        val start = System.currentTimeMillis()
        val proceed = joinPoint.proceed()
        val executionTime = System.currentTimeMillis() - start
        logger.info("/////// LogExecutionTimeAspectAnnotation - " + joinPoint.signature + " executed in " + executionTime + "ms ")
        logger.info("/////// LogExecutionTimeAspectAnnotation - AROUND FINISH  logExecutionTime annotation ///////")
        return null
    }
}
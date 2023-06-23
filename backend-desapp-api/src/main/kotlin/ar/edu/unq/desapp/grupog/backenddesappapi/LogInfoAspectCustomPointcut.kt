package ar.edu.unq.desapp.grupog.backenddesappapi

import org.aspectj.lang.annotation.After
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Aspect
@Component
@Order(0)
class LogInfoAspectCustomPointcut {

    var logger: Logger = LoggerFactory.getLogger(LogInfoAspectCustomPointcut::class.java)

    /// CUSTOM  POINTCUT////
    @Pointcut("execution(* ar.edu.unq.desapp.grupog.backenddesappapi.webservice.UserController.*(..))")
    fun methodsStarterServicePointcut() {
    }

    @Before("methodsStarterServicePointcut()")
    @Throws(Throwable::class)
    fun beforeMethods() {
        logger.info("<timestamp,user,operación/metodo, parámetros, tiempoDeEjecicion>")
    }

    @After("methodsStarterServicePointcut()")
    @Throws(Throwable::class)
    fun afterMethods() {
        logger.info("/////// LogInfoAspectCustomPointcut - AFTER POINTCUT /////")
    }
}
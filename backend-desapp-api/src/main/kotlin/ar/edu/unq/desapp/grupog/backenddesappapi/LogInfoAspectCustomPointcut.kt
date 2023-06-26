package ar.edu.unq.desapp.grupog.backenddesappapi

import ar.edu.unq.desapp.grupog.backenddesappapi.service.UserService
import ar.edu.unq.desapp.grupog.backenddesappapi.webservice.ControllerHelper
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.format.DateTimeFormatter

@Aspect
@Component
@Order(0)
class LogInfoAspectCustomPointcut : ControllerHelper() {

    var logger: Logger = LoggerFactory.getLogger(LogInfoAspectCustomPointcut::class.java)

    @Autowired private lateinit var userService: UserService

    @Around("execution(* ar.edu.unq.desapp.grupog.backenddesappapi.webservice.security.AuthController.*(..))")
    @Throws(Throwable::class)
    fun logEntryAndArgumentsOfAuthControllerAnnotation(joinPoint: ProceedingJoinPoint): Any {
        val timeStamp = DateTimeFormatter.ISO_INSTANT.format(Instant.now())
        val method = joinPoint.signature.toString().substring(40)
        val arguments = getArguments(joinPoint)
        val initialTime = System.currentTimeMillis()
        val proceed = joinPoint.proceed()
        val executionTime = System.currentTimeMillis() - initialTime

        logger.info(
            "///////// \n {} \n Method: {} \n with parameters: \n{} Execution time: {} ms \n /////////",
            timeStamp,
            method,
            arguments,
            executionTime
        )

        return proceed
    }

    @Around("execution(* ar.edu.unq.desapp.grupog.backenddesappapi.webservice.UserController.*(..))"
            + "|| execution(* ar.edu.unq.desapp.grupog.backenddesappapi.webservice.IntentionController.*(..))"
            + "|| execution(* ar.edu.unq.desapp.grupog.backenddesappapi.webservice.TransactionController.*(..))")
    @Throws(Throwable::class)
    fun logEntryAndArgumentsOfControllersAnnotation(joinPoint: ProceedingJoinPoint): Any {
        val timeStamp = DateTimeFormatter.ISO_INSTANT.format(Instant.now())
        val method = joinPoint.signature.toString().substring(40)
        val arguments = getArguments(joinPoint)
        val initialTime = System.currentTimeMillis()
        val proceed = joinPoint.proceed()
        val executionTime = System.currentTimeMillis() - initialTime
        val userName = getUserName()

        logger.info(
            "///////// \n {} \n User: {} \n Method: {} \n with parameters: \n{} Execution time: {} ms \n /////////",
            timeStamp,
            userName,
            method,
            arguments,
            executionTime
        )

        return proceed
    }

    private fun getArguments(proceedingJoinPoint: ProceedingJoinPoint): String {
        val sb = StringBuilder()

        listOf<Any>(*proceedingJoinPoint.args).forEach { argument ->
            val arg = addArgument(argument)
            sb.append("  $arg \n")
        }

        return sb.toString()
    }

    private fun addArgument(argument: Any): Any {
        return if (argument.toString().startsWith("ar.")) argument.toString().substring(25)
        else argument
    }

    private fun getUserName(): String {
        val user = userService.findByEmail(emailOfCurrentUser())
        return user.firstName + " " + user.lastName
    }
}
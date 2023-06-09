package ar.edu.unq.desapp.grupog.backenddesappapi.webservice

import ar.edu.unq.desapp.grupog.backenddesappapi.model.exceptions.IntentionNotAvailableException
import ar.edu.unq.desapp.grupog.backenddesappapi.model.exceptions.UserAlreadyRegisteredException
import org.springframework.http.*
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.*
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import javax.management.InvalidAttributeValueException

@RestControllerAdvice
class GeneralControllerAdvise {

    @ExceptionHandler(HttpMessageNotReadableException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleHttpMessageNotReadableException(ex: HttpMessageNotReadableException): ResponseEntity<Any> {
        val messageTotal = ex.message!!.split("problem")
        var finalMessage = messageTotal[1].substring(2, messageTotal[1].length)
        while (finalMessage.contains(";".single())) {
            finalMessage = finalMessage.dropLast(1)
        }
        return ResponseEntity.badRequest().body(finalMessage)
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    fun handleMethodArgumentTypeMismatchException(ex: MethodArgumentTypeMismatchException): ResponseEntity<Any> {
        val errorMessage =
            "Required request parameter '" + ex.name + "' for method parameter type " + ex.requiredType + " is not present"
        return ResponseEntity.badRequest().body(errorMessage)
    }

    @ExceptionHandler(InvalidAttributeValueException::class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    fun handleInvalidAttributeValueException(ex: InvalidAttributeValueException): ResponseEntity<Any> {
        return ResponseEntity.badRequest().body(ex.message)
    }

    @ExceptionHandler(UserAlreadyRegisteredException::class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    fun handleUserAlreadyRegisteredException(ex: UserAlreadyRegisteredException): ResponseEntity<Any> {
        return ResponseEntity.badRequest().body(ex.message)
    }

    @ExceptionHandler(IntentionNotAvailableException::class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    fun handleIntentionNotAvailableException(ex: IntentionNotAvailableException): ResponseEntity<Any> {
        return ResponseEntity.badRequest().body(ex.message)
    }
}
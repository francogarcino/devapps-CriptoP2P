package ar.edu.unq.desapp.grupog.backenddesappapi.webservice

import ar.edu.unq.desapp.grupog.backenddesappapi.service.TransactionService
import ar.edu.unq.desapp.grupog.backenddesappapi.webservice.dtos.TransactionDTO
import ar.edu.unq.desapp.grupog.backenddesappapi.webservice.mappers.TransactionMapper
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.*
import io.swagger.v3.oas.annotations.responses.*
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin
@RequestMapping("/transactions")
class TransactionController : ControllerHelper() {

    @Autowired private lateinit var transactionService: TransactionService
    private var mapper = TransactionMapper()

    @Operation(
        summary = "Get all transactions",
        description = "Get all transactions",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [
                    Content(
                        mediaType = "application/json",
                        array = ArraySchema(schema = Schema(implementation = TransactionDTO::class)),
                    )
                ]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = [
                    Content(
                        mediaType = "application/json",
                        examples = [ExampleObject(
                            value = "A error"
                        )]
                    )
                ]
            )
        ]
    )
    @GetMapping("/")
    fun getAllTransactions(request: HttpServletRequest) : ResponseEntity<Any> {
        if (jwtDoesNotExistInTheHeader(request)) {
            return ResponseEntity(messageNotAuthenticated, HttpStatus.UNAUTHORIZED)
        }
        val allTransactions = transactionService.readAll()
        return ResponseEntity.ok(allTransactions.map {
                transaction -> mapper.fromTransactionToDTO(transaction)
        })
    }

    @Operation(
        summary = "Get a transaction",
        description = "Get a transaction using the id as the unique identifier",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = TransactionDTO::class),
                    )
                ]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Bad Request",
                content = [
                    Content(
                        mediaType = "application/json",
                        examples = [ExampleObject(
                            value = "Required request parameter 'id' for method parameter type long is not present"
                        )]
                    )
                ]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = [
                    Content(
                        mediaType = "application/json",
                        examples = [ExampleObject(
                            value = "A error"
                        )]
                    )
                ]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Not Found",
                content = [
                    Content(
                        mediaType = "application/json",
                        examples = [ExampleObject(
                            value = "The received ID doesn't match with any transaction in the database"
                        )]
                    )
                ]
            )
        ]
    )
    @GetMapping("/{id}")
    fun getTransaction(request: HttpServletRequest, @PathVariable id: Long) : ResponseEntity<Any> {
        if (jwtDoesNotExistInTheHeader(request)) {
            return ResponseEntity(messageNotAuthenticated, HttpStatus.UNAUTHORIZED)
        }
        return try {
            ResponseEntity.ok().body(mapper.fromTransactionToDTO(transactionService.read(id)))
        } catch (e: NoSuchElementException) {
            ResponseEntity(e.message, HttpStatus.NOT_FOUND)
        }
    }

    @Operation(
        summary = "Cancel a transaction",
        description = "Cancel a transaction using the id as the unique identifier",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = TransactionDTO::class),
                    )
                ]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Bad Request",
                content = [
                    Content(
                        mediaType = "application/json",
                        examples = [ExampleObject(
                            value = "A error"
                        )]
                    )
                ]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = [
                    Content(
                        mediaType = "application/json",
                        examples = [ExampleObject(
                            value = "A error"
                        )]
                    )
                ]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Not Found",
                content = [
                    Content(
                        mediaType = "application/json",
                        examples = [ExampleObject(
                            value = "The received ID doesn't match with any transaction in the database"
                        )]
                    )
                ]
            )
        ]
    )
    @PutMapping("/cancelTransaction/{idTransaction}")
    fun cancelTransaction(request: HttpServletRequest, @PathVariable idTransaction: Long) : ResponseEntity<Any> {
        if (jwtDoesNotExistInTheHeader(request)) {
            return ResponseEntity(messageNotAuthenticated, HttpStatus.UNAUTHORIZED)
        }
        return try {
            val dto = mapper.fromTransactionToDTO(transactionService.cancelTransaction(idTransaction))
            ResponseEntity.ok().body(dto)
        } catch (e: NoSuchElementException) {
            ResponseEntity(e.message, HttpStatus.NOT_FOUND)
        } catch (e: Throwable) {
            ResponseEntity.badRequest().body(e.message)
        }
    }

}
package ar.edu.unq.desapp.grupog.backenddesappapi.webservice

import ar.edu.unq.desapp.grupog.backenddesappapi.service.TransactionService
import ar.edu.unq.desapp.grupog.backenddesappapi.webservice.dtos.TransactionDTO
import ar.edu.unq.desapp.grupog.backenddesappapi.webservice.mappers.TransactionMapper
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.*
import io.swagger.v3.oas.annotations.responses.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin
@RequestMapping("/transactions")
class TransactionController {

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
            )
        ]
    )
    @GetMapping("/")
    fun getAllTransactions() : ResponseEntity<List<TransactionDTO>> {
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
    fun getTransaction(@PathVariable id: Long) : ResponseEntity<Any> {
        return try {
            ResponseEntity.ok().body(mapper.fromTransactionToDTO(transactionService.read(id)))
        } catch (e: Exception) {
            ResponseEntity(e.message, HttpStatus.NOT_FOUND)
        }
    }

}
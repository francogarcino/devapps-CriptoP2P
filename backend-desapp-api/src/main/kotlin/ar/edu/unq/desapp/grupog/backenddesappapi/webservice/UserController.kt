package ar.edu.unq.desapp.grupog.backenddesappapi.webservice

import ar.edu.unq.desapp.grupog.backenddesappapi.model.User
import ar.edu.unq.desapp.grupog.backenddesappapi.service.*
import ar.edu.unq.desapp.grupog.backenddesappapi.webservice.dtos.*
import ar.edu.unq.desapp.grupog.backenddesappapi.webservice.mappers.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.*
import io.swagger.v3.oas.annotations.responses.*
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController
import java.lang.Exception

@RestController
@CrossOrigin
@RequestMapping("/users")
class UserController {
    @Autowired private lateinit var userService: UserService
    @Autowired private lateinit var intentionService: IntentionService
    private var userMapper = UserMapper()
    private var intentionMapper = IntentionMapper()
    private var transactionMapper = TransactionMapper()

    @Operation(
        summary = "Get a user",
        description = "Get a user using the id as the unique identifier",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = UserDTO::class),
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
                            value = "The received ID doesn't match with any user in the database"
                        )]
                    )
                ]
            )
        ]
    )
    @GetMapping("/{id}")
    fun getUser(@PathVariable id: Long) : ResponseEntity<Any>{
        return try {
            ResponseEntity.ok().body(userMapper.fromUserToDTO(userService.read(id)))
        } catch (e: Exception) {
            ResponseEntity(e.message, HttpStatus.NOT_FOUND)
        }
    }

    @Operation(
        summary = "Get all users",
        description = "Get all users",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [
                    Content(
                        mediaType = "application/json",
                        array = ArraySchema(schema = Schema(implementation = UserDTO::class)),
                    )
                ]
            )
        ]
    )
    @GetMapping("/")
    fun getAllUsers() : ResponseEntity<List<UserDTO>>{
        val users = userService.readAll()
        return ResponseEntity.ok(users.map { u -> userMapper.fromUserToDTO(u) })
    }

    @Operation(
        summary = "Create a user",
        description = "Create a user using the email, cvu and wallet as unique identifiers",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = UserDTO::class),
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
            )
        ]
    )
    @PostMapping("/register")
    fun createUser(
        @RequestBody @Valid user: User
    ) : ResponseEntity<Any> {
        val dto = userMapper.fromUserToDTO(userService.create(user))
        return ResponseEntity.ok().body(dto)
    }

    @Operation(
        summary = "Create an intention",
        description = "Create an intention with a registered user by validating him by his id",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = IntentionDTO::class),
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
                responseCode = "404",
                description = "Not Found",
                content = [
                    Content(
                        mediaType = "application/json",
                        examples = [ExampleObject(
                            value = "The received ID doesn't match with any user in the database"
                        )]
                    )
                ]
            )
        ]
    )
    @PostMapping("/{id}/createIntention")
    fun createIntention(@PathVariable id : Long, @RequestBody @Valid newIntentionDTO : IntentionDTO) : ResponseEntity<Any> {
        return try {
            val user = userService.read(id)
            val newIntention = intentionMapper.fromDTOToIntention(newIntentionDTO, user)
            val dto = intentionMapper.fromIntentionToDTO(intentionService.create(newIntention))
            ResponseEntity.ok().body(dto)
        } catch (e : Exception) {
            ResponseEntity(e.message, HttpStatus.NOT_FOUND)
        }
    }

    @Operation(
        summary = "Create a transaction",
        description = "Create a transaction",
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
                responseCode = "404",
                description = "Not Found",
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
    @PostMapping("/{idUser}/{idIntention}")
    fun createTransaction(@PathVariable idUser: Long, @PathVariable idIntention: Long) : ResponseEntity<Any> {
        return try {
            val dto = transactionMapper.fromTransactionToDTO(userService.beginTransaction(idUser, idIntention))
            ResponseEntity.ok().body(dto)
        } catch (e: Throwable) {
            ResponseEntity.badRequest().body(e.message)
        } catch (e : Exception) {
            ResponseEntity(e.message, HttpStatus.NOT_FOUND)
        }
    }

    @Operation(
        summary = "Register a transfer",
        description = "Register a transfer in a transaction using the id as the unique identifier",
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
                responseCode = "404",
                description = "Not Found",
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
    @PutMapping("/{idUser}/{idTransaction}/registerTransfer")
    fun registerTransfer(@PathVariable idUser: Long, @PathVariable idTransaction: Long) : ResponseEntity<Any> {
        return try {
            val dto = transactionMapper.fromTransactionToDTO(userService.registerTransfer(idUser, idTransaction))
            ResponseEntity.ok().body(dto)
        } catch (e: Throwable) {
            ResponseEntity.badRequest().body(e.message)
        } catch (e : Exception) {
            ResponseEntity(e.message, HttpStatus.NOT_FOUND)
        }
    }

    @Operation(
        summary = "Register a crypto release",
        description = "Register a crypto release in a transaction using the id as the unique identifier",
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
                responseCode = "404",
                description = "Not Found",
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
    @PutMapping("/{idUser}/{idTransaction}/registerRelease")
    fun registerReleaseCrypto(@PathVariable idUser: Long, @PathVariable idTransaction: Long) : ResponseEntity<Any> {
        return try {
            val dto = transactionMapper.fromTransactionToDTO(userService.registerReleaseCrypto(idUser, idTransaction))
            ResponseEntity.ok().body(dto)
        } catch (e: Throwable) {
            ResponseEntity.badRequest().body(e.message)
        } catch (e : Exception) {
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
                responseCode = "404",
                description = "Not Found",
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
    @PutMapping("/{idUser}/{idTransaction}/cancelTransaction")
    fun cancelTransaction(@PathVariable idUser: Long, @PathVariable idTransaction: Long) : ResponseEntity<Any> {
        return try {
            val dto = transactionMapper.fromTransactionToDTO(userService.cancelTransaction(idUser, idTransaction))
            ResponseEntity.ok().body(dto)
        } catch (e: Throwable) {
            ResponseEntity.badRequest().body(e.message)
        } catch (e : Exception) {
            ResponseEntity(e.message, HttpStatus.NOT_FOUND)
        }
    }

}
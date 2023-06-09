package ar.edu.unq.desapp.grupog.backenddesappapi.webservice

import ar.edu.unq.desapp.grupog.backenddesappapi.model.CryptoVolume
import ar.edu.unq.desapp.grupog.backenddesappapi.model.exceptions.OutOfRangePriceException
import ar.edu.unq.desapp.grupog.backenddesappapi.service.*
import ar.edu.unq.desapp.grupog.backenddesappapi.webservice.dtos.*
import ar.edu.unq.desapp.grupog.backenddesappapi.webservice.mappers.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.*
import io.swagger.v3.oas.annotations.responses.*
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController
import java.lang.Exception
import java.time.LocalDateTime

@RestController
@CrossOrigin
@RequestMapping("/users")
class UserController : ControllerHelper() {

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
                            value = "The received ID doesn't match with any user in the database"
                        )]
                    )
                ]
            )
        ]
    )
    @GetMapping("/{id}")
    fun getUser(request: HttpServletRequest, @PathVariable id: Long) : ResponseEntity<Any>{
        if (jwtDoesNotExistInTheHeader(request)) {
            return ResponseEntity(messageNotAuthenticated, HttpStatus.UNAUTHORIZED)
        }
        return try {
            ResponseEntity.ok().body(userMapper.fromUserToDTO(userService.read(id)))
        } catch (e: NoSuchElementException) {
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
    fun getAllUsers(request: HttpServletRequest) : ResponseEntity<Any>{
        if (jwtDoesNotExistInTheHeader(request)) {
            return ResponseEntity(messageNotAuthenticated, HttpStatus.UNAUTHORIZED)
        }
        val users = userService.readAll()
        return ResponseEntity.ok(users.map { u -> userMapper.fromUserToDTO(u) })
    }

    @Operation(
        summary = "Create an intention",
        description = "Create an intention with the user logged in",
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
                            value = "The received ID doesn't match with any user in the database"
                        )]
                    )
                ]
            )
        ]
    )
    @PostMapping("/createIntention")
    fun createIntention(request: HttpServletRequest,
                        @RequestBody @Valid newIntentionDTO : IntentionCreateDTO) : ResponseEntity<Any> {
        if (jwtDoesNotExistInTheHeader(request)) {
            return ResponseEntity(messageNotAuthenticated, HttpStatus.UNAUTHORIZED)
        }
        return try {
            val user = userService.findByEmail(emailOfCurrentUser())
            val newIntention = intentionMapper.fromCreateDTOToIntention(newIntentionDTO, user)
            val dto = intentionMapper.fromIntentionToDTO(intentionService.create(newIntention))
            ResponseEntity.ok().body(dto)
        } catch (e : NoSuchElementException) {
            ResponseEntity(e.message, HttpStatus.NOT_FOUND)
        } catch (e : OutOfRangePriceException) {
            ResponseEntity(e.message, HttpStatus.BAD_REQUEST)
        }
    }

    @Operation(
            summary = "Get crypto volume",
            description = "Obtains crypto volume of the given user between initial and final date"
    )
    @ApiResponses(
            value = [
                ApiResponse(
                        responseCode = "200",
                        description = "Success",
                        content = [
                            Content(
                                    mediaType = "application/json",
                                    schema = Schema(implementation = CryptoVolume::class)
                            )
                        ]
                ),
                ApiResponse(
                        responseCode = "400",
                        description = "Bad request",
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
                        description = "Not found",
                        content = [
                            Content(
                                    mediaType = "application/json",
                                    examples = [ExampleObject(
                                            value = "The received ID doesn't match with any user in the database"
                                    )]
                            )
                        ]
                ),
            ]
    )
    @GetMapping("/{userId}/cryptoVolume")
    fun getCryptoVolume(request: HttpServletRequest,
                        @PathVariable userId: Long,
                        @RequestBody @Valid dateRange: DateRangeDTO): ResponseEntity<Any> {
        return try {
            if (jwtDoesNotExistInTheHeader(request)) {
                return ResponseEntity(messageNotAuthenticated, HttpStatus.UNAUTHORIZED)
            }
            val init : LocalDateTime = LocalDateTime.of(dateRange.initYear,dateRange.initMonth,dateRange.initDay,0,0,0)
            val end : LocalDateTime = LocalDateTime.of(dateRange.endYear,dateRange.endMonth,dateRange.endDay,0,0,0)
            val cryptoVolume = userService.getCryptoVolume(userId, init, end)
            return ResponseEntity.ok().body(cryptoVolume)
        } catch (e: Exception) {
            ResponseEntity(e.message, HttpStatus.NOT_FOUND)
        } catch (e: Throwable) {
            ResponseEntity.badRequest().body(e.message)
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
                            value = "A error"
                        )]
                    )
                ]
            )
        ]
    )
    @PostMapping("/createTransaction/{idIntention}")
    fun createTransaction(request: HttpServletRequest,
                          @PathVariable idIntention: Long) : ResponseEntity<Any> {
        if (jwtDoesNotExistInTheHeader(request)) {
            return ResponseEntity(messageNotAuthenticated, HttpStatus.UNAUTHORIZED)
        }
        return try {
            val user = userService.findByEmail(emailOfCurrentUser())
            val dto = transactionMapper.fromTransactionToDTO(userService.beginTransaction(user.id!!, idIntention))
            ResponseEntity.ok().body(dto)
        } catch (e: NoSuchElementException) {
            ResponseEntity(e.message, HttpStatus.NOT_FOUND)
        } catch (e: Throwable) {
            ResponseEntity.badRequest().body(e.message)
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
                            value = "A error"
                        )]
                    )
                ]
            )
        ]
    )
    @PutMapping("/registerTransfer/{idTransaction}")
    fun registerTransfer(request: HttpServletRequest,
                         @PathVariable idTransaction: Long) : ResponseEntity<Any> {
        if (jwtDoesNotExistInTheHeader(request)) {
            return ResponseEntity(messageNotAuthenticated, HttpStatus.UNAUTHORIZED)
        }
        return try {
            val user = userService.findByEmail(emailOfCurrentUser())
            val dto = transactionMapper.fromTransactionToDTO(userService.registerTransfer(user.id!!, idTransaction))
            ResponseEntity.ok().body(dto)
        } catch (e: NoSuchElementException) {
            ResponseEntity(e.message, HttpStatus.NOT_FOUND)
        } catch (e: Throwable) {
            ResponseEntity.badRequest().body(e.message)
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
                            value = "A error"
                        )]
                    )
                ]
            )
        ]
    )
    @PutMapping("/registerRelease/{idTransaction}")
    fun registerReleaseCrypto(request: HttpServletRequest,
                              @PathVariable idTransaction: Long) : ResponseEntity<Any> {
        if (jwtDoesNotExistInTheHeader(request)) {
            return ResponseEntity(messageNotAuthenticated, HttpStatus.UNAUTHORIZED)
        }
        return try {
            val user = userService.findByEmail(emailOfCurrentUser())
            val dto = transactionMapper.fromTransactionToDTO(userService.registerReleaseCrypto(user.id!!, idTransaction))
            ResponseEntity.ok().body(dto)
        } catch (e: NoSuchElementException) {
            ResponseEntity(e.message, HttpStatus.NOT_FOUND)
        } catch (e: Throwable) {
            ResponseEntity.badRequest().body(e.message)
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
                            value = "A error"
                        )]
                    )
                ]
            )
        ]
    )
    @PutMapping("/cancelTransaction/{idTransaction}")
    fun cancelTransaction(request: HttpServletRequest,
                          @PathVariable idTransaction: Long) : ResponseEntity<Any> {
        if (jwtDoesNotExistInTheHeader(request)) {
            return ResponseEntity(messageNotAuthenticated, HttpStatus.UNAUTHORIZED)
        }
        return try {
            val user = userService.findByEmail(emailOfCurrentUser())
            val dto = transactionMapper.fromTransactionToDTO(userService.cancelTransaction(user.id!!, idTransaction))
            ResponseEntity.ok().body(dto)
        } catch (e: NoSuchElementException) {
            ResponseEntity(e.message, HttpStatus.NOT_FOUND)
        } catch (e: Throwable) {
            ResponseEntity.badRequest().body(e.message)
        }
    }

    @Operation(
        summary = "Get all users with stats",
        description = "Get all users with stats",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [
                    Content(
                        mediaType = "application/json",
                        array = ArraySchema(schema = Schema(implementation = UserStatsDTO::class)),
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
    @GetMapping("/stats")
    fun getUsersWithStats(request: HttpServletRequest) : ResponseEntity<Any>{
        if (jwtDoesNotExistInTheHeader(request)) {
            return ResponseEntity(messageNotAuthenticated, HttpStatus.UNAUTHORIZED)
        }
        val pairs = userService.allUserStats()
        val stats = pairs.map { p -> userMapper.fromDataToStatsDTO(p) }
        return ResponseEntity.ok().body(stats)
    }

}
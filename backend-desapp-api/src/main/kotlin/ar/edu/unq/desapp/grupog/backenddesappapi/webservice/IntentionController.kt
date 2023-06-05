package ar.edu.unq.desapp.grupog.backenddesappapi.webservice

import ar.edu.unq.desapp.grupog.backenddesappapi.service.IntentionService
import ar.edu.unq.desapp.grupog.backenddesappapi.webservice.dtos.IntentionDTO
import ar.edu.unq.desapp.grupog.backenddesappapi.webservice.mappers.IntentionMapper
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.*
import io.swagger.v3.oas.annotations.responses.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@CrossOrigin
@RequestMapping("/intentions")
class IntentionController {

    @Autowired private lateinit var intentionService: IntentionService
    private var mapper = IntentionMapper()
    private val messageUnauthorized = "It is not authenticated. Please log in"

    @Operation(
        summary = "Get all intentions",
        description = "Get all intetions",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [
                    Content(
                        mediaType = "application/json",
                        array = ArraySchema(schema = Schema(implementation = IntentionDTO::class)),
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
    fun getAllIntentions(@CookieValue("jwt") jwt: String?) : ResponseEntity<Any> {
        if (jwt.isNullOrBlank()) {
            return ResponseEntity(messageUnauthorized, HttpStatus.UNAUTHORIZED)
        }
        val allIntentions = intentionService.readAll()
        return ResponseEntity.ok(allIntentions.map {
            intention -> mapper.fromIntentionToDTO(intention)
        })
    }

    @Operation(
        summary = "Get an intention",
        description = "Get an intention using the id as the unique identifier",
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
                            value = "There is no intention with that id"
                        )]
                    )
                ]
            )
        ]
    )
    @GetMapping("/{id}")
    fun getIntention(@CookieValue("jwt") jwt: String?, @PathVariable id: Long) : ResponseEntity<Any> {
        if (jwt.isNullOrBlank()) {
            return ResponseEntity(messageUnauthorized, HttpStatus.UNAUTHORIZED)
        }
        return try {
            ResponseEntity.ok().body(mapper.fromIntentionToDTO(intentionService.read(id)))
        } catch (e: NoSuchElementException) {
            ResponseEntity(e.message,HttpStatus.NOT_FOUND)
        }
    }

    @Operation(
        summary = "Get all active intentions",
        description = "Get all active intetions",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [
                    Content(
                        mediaType = "application/json",
                        array = ArraySchema(schema = Schema(implementation = IntentionDTO::class)),
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
    @GetMapping("/activeIntentions")
    fun getActiveIntentions(@CookieValue("jwt") jwt: String?) : ResponseEntity<Any> {
        if (jwt.isNullOrBlank()) {
            return ResponseEntity(messageUnauthorized, HttpStatus.UNAUTHORIZED)
        }
        val intentions = intentionService.getActiveIntentions()
        return ResponseEntity.ok(intentions.map { intention ->
            mapper.fromIntentionToDTO(intention)
        })
    }
}
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

@RestController
@CrossOrigin
@RequestMapping("/intentions")
class IntentionController {
    @Autowired private lateinit var intentionService: IntentionService
    private var mapper = IntentionMapper()

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
            )
        ]
    )
    @GetMapping("/")
    fun getAllIntentions() : ResponseEntity<List<IntentionDTO>> {
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
    fun getIntention(@PathVariable id: Long) : ResponseEntity<Any> {
        return try {
            ResponseEntity.ok().body(mapper.fromIntentionToDTO(intentionService.read(id)))
        } catch (e: Exception) {
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
            )
        ]
    )
    @GetMapping("/activeIntentions")
    fun getActiveIntentions() : ResponseEntity<List<IntentionDTO>> {
        val intentions = intentionService.getActiveIntentions()
        return ResponseEntity.ok(intentions.map { intention ->
            mapper.fromIntentionToDTO(intention)
        })
    }
}
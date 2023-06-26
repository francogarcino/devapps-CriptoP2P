package ar.edu.unq.desapp.grupog.backenddesappapi.webservice

import ar.edu.unq.desapp.grupog.backenddesappapi.model.CryptoActiveName
import ar.edu.unq.desapp.grupog.backenddesappapi.service.impl.ExternalApisServiceImpl
import ar.edu.unq.desapp.grupog.backenddesappapi.service.impl.PriceWithTime
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin
@RequestMapping("/prices")
class CryptoActivesController : ControllerHelper() {
    @Autowired lateinit var apisService : ExternalApisServiceImpl
    @Operation(
        summary = "Get the price of a CryptoActive",
        description = "Get the actual price of a CryptoActive",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = Double::class),
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
    @GetMapping("/crypto/{activeName}")
    fun crypto(@PathVariable activeName: CryptoActiveName) : ResponseEntity<Any> {
        return ResponseEntity.ok(apisService.getCryptoPrice(activeName))
    }

    @Operation(
        summary = "Get the prices in the last 24 hours for a CryptoActive",
        description = "Know the prices that a CryptoActive has in the last 24 hours every 3 minutes",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = PriceWithTime::class),
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
    @GetMapping("/daily/{activeName}")
    fun daily(@PathVariable activeName: CryptoActiveName): ResponseEntity<List<PriceWithTime>> {
        return ResponseEntity.ok().body(apisService.getLast24Hours(activeName))
    }
}
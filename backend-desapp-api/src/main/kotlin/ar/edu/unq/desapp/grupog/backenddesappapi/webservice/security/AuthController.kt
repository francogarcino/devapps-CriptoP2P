package ar.edu.unq.desapp.grupog.backenddesappapi.webservice.security

import ar.edu.unq.desapp.grupog.backenddesappapi.service.UserService
import ar.edu.unq.desapp.grupog.backenddesappapi.webservice.ControllerHelper
import ar.edu.unq.desapp.grupog.backenddesappapi.webservice.dtos.LoginDTO
import ar.edu.unq.desapp.grupog.backenddesappapi.webservice.dtos.UserCreateDTO
import ar.edu.unq.desapp.grupog.backenddesappapi.webservice.dtos.UserDTO
import ar.edu.unq.desapp.grupog.backenddesappapi.webservice.mappers.UserMapper
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.*
import io.swagger.v3.oas.annotations.responses.*
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@CrossOrigin
class AuthController : ControllerHelper() {

    @Autowired private lateinit var authenticationManager: AuthenticationManager
    @Autowired private lateinit var jwtGenerator: JwtGenerator
    @Autowired private lateinit var userService: UserService
    private var userMapper = UserMapper()

    @Operation(
        summary = "Log in to the app",
        description = "Log in to the app",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [
                    Content(
                    mediaType = "application/json",
                        examples = [ExampleObject(
                        value = "You are logged in correctly"
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
            )
        ]
    )
    @PostMapping("/login")
    fun login(@RequestBody @Valid loginData: LoginDTO): ResponseEntity<Any> {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(loginData.email, loginData.password)
        )
        SecurityContextHolder.getContext().authentication = authentication
        val token = jwtGenerator.generateToken(authentication)
        return ResponseEntity(TokenInfo(token), HttpStatus.OK)
    }

    @Operation(
        summary = "Log out to the app",
        description = "Log out to the app",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [
                    Content(
                        mediaType = "application/json",
                        examples = [ExampleObject(
                            value = "You successfully logged out"
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
            )
        ]
    )
    @PostMapping("/log-out")
    fun logout(request: HttpServletRequest): ResponseEntity<Any> {
        if (jwtDoesNotExistInTheHeader(request)) {
            return ResponseEntity(messageNotAuthenticated, HttpStatus.UNAUTHORIZED)
        }
        jwtGenerator.removeToken()
        return ResponseEntity("You successfully logged out", HttpStatus.OK)
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
    @PostMapping("/users/register")
    fun createUser(
        @RequestBody @Valid user: UserCreateDTO
    ) : ResponseEntity<Any> {
        val newUser = userMapper.fromCreateDTOToUser(user)
        val dto = userMapper.fromUserToDTO(userService.create(newUser))
        return ResponseEntity.ok().body(dto)
    }
}
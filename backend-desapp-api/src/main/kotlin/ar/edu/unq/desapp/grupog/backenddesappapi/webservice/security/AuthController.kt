package ar.edu.unq.desapp.grupog.backenddesappapi.webservice.security

import ar.edu.unq.desapp.grupog.backenddesappapi.service.UserService
import ar.edu.unq.desapp.grupog.backenddesappapi.webservice.dtos.LoginDTO
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.*
import io.swagger.v3.oas.annotations.responses.*
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import kotlin.NoSuchElementException

@RestController
@CrossOrigin
class AuthController {

    @Autowired private lateinit var userService: UserService

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
    fun login(@RequestBody @Valid loginData: LoginDTO, response: HttpServletResponse): ResponseEntity<Any> {
        try {
            val user = userService.findByEmail(loginData.email!!)
            if (user.password != loginData.password!!) {
                return ResponseEntity("Password is incorrect", HttpStatus.UNAUTHORIZED)
            }
            addCookie(response)
            return ResponseEntity("You are logged in correctly", HttpStatus.OK)
        } catch (e: NoSuchElementException) {
            return ResponseEntity(e.message!!, HttpStatus.UNAUTHORIZED)
        }
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
                            value = "It is not authenticated. Please log in"
                        )]
                    )
                ]
            )
        ]
    )
    @PostMapping("/logout")
    fun logout(@CookieValue("jwt") jwt: String?, response: HttpServletResponse): ResponseEntity<Any> {
        if (jwt.isNullOrBlank()) {
            return ResponseEntity("It is not authenticated. Please log in", HttpStatus.UNAUTHORIZED)
        }
        val cookie = Cookie("jwt", null)
        cookie.isHttpOnly = true
        cookie.maxAge = 0
        response.addCookie(cookie)
        return ResponseEntity("You successfully logged out", HttpStatus.OK)
    }

    private fun addCookie(response: HttpServletResponse) {
        val jwt = Jwts.builder()
            .setExpiration(Date(System.currentTimeMillis() + 86400000))
            .signWith(Keys.secretKeyFor(SignatureAlgorithm.HS512))
            .compact()

        val cookie = Cookie("jwt", jwt)
        cookie.isHttpOnly = false
        response.addCookie(cookie)
    }
}
package ar.edu.unq.desapp.grupog.backenddesappapi.webservice

import ar.edu.unq.desapp.grupog.backenddesappapi.webservice.security.JwtGenerator
import io.jsonwebtoken.Jwts
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpHeaders
import org.springframework.util.StringUtils
import javax.crypto.SecretKey

open class ControllerHelper {

    protected val messageNotAuthenticated = "It is not authenticated. Please log in"
    private var passwordEncrypt: SecretKey? = null
    private lateinit var currentHeader: String

    protected fun jwtDoesNotExistInTheHeader(request: HttpServletRequest): Boolean {
        currentHeader = request.getHeader(HttpHeaders.AUTHORIZATION)
        return !existJWT(currentHeader)
    }

    private fun existJWT(jwt: String?): Boolean {
        return StringUtils.hasText(jwt) &&
                jwt!!.startsWith("Bearer ")
                && jwt.substring(7, jwt.length).isNotEmpty()
    }

    protected fun emailOfCurrentUser(): String {
        passwordEncrypt = JwtGenerator.JWT_SECRET_KEY
        val header = currentHeader.substring(7, currentHeader.length)
        return Jwts.parser().setSigningKey(passwordEncrypt).parseClaimsJws(header).body.subject
    }
}
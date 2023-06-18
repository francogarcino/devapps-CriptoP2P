package ar.edu.unq.desapp.grupog.backenddesappapi.webservice

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpHeaders
import org.springframework.util.StringUtils

open class ControllerHelper {

    protected val messageNotAuthenticated = "It is not authenticated. Please log in"

    protected fun jwtDoesNotExistInTheHeader(request: HttpServletRequest): Boolean {
        val currentHeader = request.getHeader(HttpHeaders.AUTHORIZATION)
        return !existJWT(currentHeader)
    }

    private fun existJWT(jwt: String?): Boolean {
        return StringUtils.hasText(jwt) &&
                jwt!!.startsWith("Bearer ")
                && jwt.substring(7, jwt.length).isNotEmpty()
    }
}
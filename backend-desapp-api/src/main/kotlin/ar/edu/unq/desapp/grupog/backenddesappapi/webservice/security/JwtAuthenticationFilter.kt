package ar.edu.unq.desapp.grupog.backenddesappapi.webservice.security

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException

class JwtAuthenticationFilter : OncePerRequestFilter() {

    @Autowired
    private lateinit var customUsersDetailsService: CustomUsersDetailsService

    @Autowired
    private lateinit var jwtGenerator: JwtGenerator

    private fun getRequestToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION)
        return if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            bearerToken.substring(7, bearerToken.length)
        } else null
    }

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = getRequestToken(request)
        if (StringUtils.hasText(token) && jwtGenerator.validateToken(token!!)) {
            val username = jwtGenerator.getJWTUsername(token)
            val userDetails = customUsersDetailsService.loadUserByUsername(username)
            val authenticationToken = UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.authorities)
            authenticationToken.details = WebAuthenticationDetailsSource().buildDetails(request)
            SecurityContextHolder.getContext().authentication = authenticationToken
        }
        filterChain.doFilter(request, response)
    }
}
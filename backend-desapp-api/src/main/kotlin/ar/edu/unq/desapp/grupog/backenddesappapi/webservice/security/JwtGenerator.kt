package ar.edu.unq.desapp.grupog.backenddesappapi.webservice.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtGenerator {

    private var JWT_SECRET_KEY: SecretKey? = null

    fun generateToken(authentication: Authentication): String {
        val username: String = authentication.name
        val currentTime = Date()
        val expirationToken = Date(currentTime.time + 86400000L)
        JWT_SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512)

        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(currentTime)
            .setExpiration(expirationToken)
            .signWith(JWT_SECRET_KEY)
            .compact()
    }

    fun getJWTUsername(token: String): String {
        val claims = Jwts.parser().setSigningKey(JWT_SECRET_KEY).parseClaimsJws(token).body
        return claims.subject
    }

    fun validateToken(token: String): Boolean {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET_KEY).parseClaimsJws(token)
            return true
        } catch (e: Exception) {
            throw AuthenticationCredentialsNotFoundException("JWT expired or is incorrect")
        }
    }

    fun removeToken() {
        JWT_SECRET_KEY = null
    }

    fun getJwtSecretKey() = JWT_SECRET_KEY
}
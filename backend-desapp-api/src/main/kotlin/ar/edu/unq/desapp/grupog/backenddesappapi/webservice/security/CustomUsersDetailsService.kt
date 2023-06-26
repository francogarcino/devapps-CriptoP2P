package ar.edu.unq.desapp.grupog.backenddesappapi.webservice.security

import ar.edu.unq.desapp.grupog.backenddesappapi.persistence.UserDAO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class CustomUsersDetailsService : UserDetailsService {

    @Autowired
    private lateinit var userDAO: UserDAO

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(email: String): UserDetails {
        val user = userDAO.findByEmail(email)
            .orElseThrow { UsernameNotFoundException("User not found") }
        return User(user.email, BCryptPasswordEncoder().encode(user.password), listOf())
    }
}
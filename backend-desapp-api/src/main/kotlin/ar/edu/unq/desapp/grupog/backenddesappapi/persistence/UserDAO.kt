package ar.edu.unq.desapp.grupog.backenddesappapi.persistence

import ar.edu.unq.desapp.grupog.backenddesappapi.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserDAO : JpaRepository<User, Long> {
}
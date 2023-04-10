package ar.edu.unq.desapp.grupog.backenddesappapi.persistence

import ar.edu.unq.desapp.grupog.backenddesappapi.model.User
import org.springframework.data.repository.CrudRepository

interface UserDAO : CrudRepository<User, Long> {
}
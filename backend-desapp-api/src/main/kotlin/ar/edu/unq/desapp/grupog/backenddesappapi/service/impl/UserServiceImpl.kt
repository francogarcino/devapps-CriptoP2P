package ar.edu.unq.desapp.grupog.backenddesappapi.service.impl

import ar.edu.unq.desapp.grupog.backenddesappapi.model.User
import ar.edu.unq.desapp.grupog.backenddesappapi.persistence.UserDAO
import ar.edu.unq.desapp.grupog.backenddesappapi.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.lang.RuntimeException
import javax.transaction.Transactional

@Transactional
@Service
class UserServiceImpl : UserService {
    @Autowired private lateinit var userDAO: UserDAO

    override fun create(user: User) = userDAO.save(user)
    override fun update(user: User): User {
        return if (userDAO.existsById(user.id!!)) {
            userDAO.save(user)
        } else { throw RuntimeException("The user to update does not exists") }
    }

    override fun read(userId: Long): User {
        val daoResponse = userDAO.findById(userId)
        if (daoResponse.isPresent) return daoResponse.get()
        else throw RuntimeException("The received ID doesn't match with any user in the database")
    }

    override fun readAll(): List<User> = userDAO.findAll().toList()
    override fun delete(userId: Long) { userDAO.deleteById(userId) }

}
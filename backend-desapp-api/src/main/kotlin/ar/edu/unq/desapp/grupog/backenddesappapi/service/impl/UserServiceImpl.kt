package ar.edu.unq.desapp.grupog.backenddesappapi.service.impl

import ar.edu.unq.desapp.grupog.backenddesappapi.model.User
import ar.edu.unq.desapp.grupog.backenddesappapi.persistence.UserDAO
import ar.edu.unq.desapp.grupog.backenddesappapi.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.RuntimeException

@Transactional
@Service
class UserServiceImpl : UserService {
    @Autowired private lateinit var userDAO: UserDAO

    override fun create(entity: User) = userDAO.save(entity)
    override fun update(entity: User): User {
        return if (userDAO.existsById(entity.id!!)) {
            userDAO.save(entity)
        } else { throw RuntimeException("The user to update does not exists") }
    }

    override fun read(entityId: Long): User {
        val daoResponse = userDAO.findById(entityId)
        if (daoResponse.isPresent) return daoResponse.get()
        else throw RuntimeException("The received ID doesn't match with any user in the database")
    }

    override fun readAll(): List<User> = userDAO.findAll().toList()
    override fun delete(entityId: Long) { userDAO.deleteById(entityId) }
    override fun deleteAll() {
        userDAO.deleteAll()
    }

}
package ar.edu.unq.desapp.grupog.backenddesappapi.service.impl

import ar.edu.unq.desapp.grupog.backenddesappapi.model.Transaction
import ar.edu.unq.desapp.grupog.backenddesappapi.model.User
import ar.edu.unq.desapp.grupog.backenddesappapi.persistence.UserDAO
import ar.edu.unq.desapp.grupog.backenddesappapi.service.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.RuntimeException

@Transactional
@Service
class UserServiceImpl : UserService {

    @Autowired private lateinit var userDAO: UserDAO
    @Autowired private lateinit var transactionService: TransactionService
    @Autowired private lateinit var intentionService: IntentionService

    override fun create(entity: User): User {
        return try {
            userDAO.save(entity)
        } catch (e: Exception) {
            throw RuntimeException(e.message)
        }
    }

    override fun update(entity: User): User {
        return if (userDAO.existsById(entity.id!!)) {
            userDAO.save(entity)
        } else { throw NoSuchElementException("The user to update does not exists") }
    }

    override fun read(entityId: Long): User {
        val daoResponse = userDAO.findById(entityId)
        if (daoResponse.isPresent) return daoResponse.get()
        else throw NoSuchElementException("The received ID doesn't match with any user in the database")
    }

    override fun readAll(): List<User> = userDAO.findAll().toList()

    override fun beginTransaction(userId: Long, intentionId: Long): Transaction {
        val user = read(userId)
        val intention = intentionService.read(intentionId)

        val transaction = user.beginTransaction(intention)
        return transactionService.create(transaction)
    }

    override fun registerTransfer(userId: Long, transactionId: Long): Transaction {
        val transaction = transactionService.read(transactionId)
        val user = read(userId)

        user.transferMoneyToBankAccount(transaction)
        return transactionService.update(transaction)
    }

    override fun registerReleaseCrypto(userId: Long, transactionId: Long): Transaction {
        val transaction = transactionService.read(transactionId)
        val user = read(userId)

        user.releaseCrypto(transaction)
        return transactionService.update(transaction)
    }

    override fun cancelTransaction(userId: Long, transactionId: Long): Transaction {
        val transaction = transactionService.read(transactionId)
        val user = read(userId)

        user.cancelTransaction(transaction)
        return transactionService.update(transaction)
    }

    override fun delete(entityId: Long) { userDAO.deleteById(entityId) }

    override fun deleteAll() { userDAO.deleteAll() }

}
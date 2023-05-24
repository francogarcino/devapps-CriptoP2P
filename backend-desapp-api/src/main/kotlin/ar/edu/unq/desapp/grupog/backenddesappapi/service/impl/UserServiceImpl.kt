package ar.edu.unq.desapp.grupog.backenddesappapi.service.impl

import ar.edu.unq.desapp.grupog.backenddesappapi.model.CryptoVolume
import ar.edu.unq.desapp.grupog.backenddesappapi.model.Transaction
import ar.edu.unq.desapp.grupog.backenddesappapi.model.User
import ar.edu.unq.desapp.grupog.backenddesappapi.persistence.IntentionDAO
import ar.edu.unq.desapp.grupog.backenddesappapi.persistence.TransactionDAO
import ar.edu.unq.desapp.grupog.backenddesappapi.persistence.UserDAO
import ar.edu.unq.desapp.grupog.backenddesappapi.service.UserService
import ar.edu.unq.desapp.grupog.backenddesappapi.webservice.dtos.CryptoVolumeDTO
import ar.edu.unq.desapp.grupog.backenddesappapi.service.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.RuntimeException
import java.time.LocalDateTime

@Transactional
@Service
class UserServiceImpl : UserService {

    @Autowired private lateinit var userDAO: UserDAO
    @Autowired private lateinit var transactionDAO: TransactionDAO
    override fun getCryptoVolume(user: User, initialDate: LocalDateTime, finalDate: LocalDateTime) : CryptoVolume {
        val date = LocalDateTime.now()
        val arsAmount = transactionDAO.getFinishedTransactions().filter { t -> t.user_whoCreate().id == user.id }
//        CryptoVolume(date, )
    }

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
        } else { throw RuntimeException("The user to update does not exists") }
    }

    override fun read(entityId: Long): User {
        val daoResponse = userDAO.findById(entityId)
        if (daoResponse.isPresent) return daoResponse.get()
        else throw RuntimeException("The received ID doesn't match with any user in the database")
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

    override fun allUserStats(): List<Pair<User, Int>> {
        val tuples = userDAO.getUsers()
        val pairs = mutableListOf<Pair<User, Int>>()
        for (i in IntRange(0, tuples.size-1)) {
            pairs.add(Pair(tuples.get(i).get(0), tuples.get(i).get(1)) as Pair<User, Int>)
        }
        return pairs
    }

    override fun delete(entityId: Long) { userDAO.deleteById(entityId) }

    override fun deleteAll() { userDAO.deleteAll() }

}
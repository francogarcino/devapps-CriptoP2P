package ar.edu.unq.desapp.grupog.backenddesappapi.service.impl

import ar.edu.unq.desapp.grupog.backenddesappapi.model.*
import ar.edu.unq.desapp.grupog.backenddesappapi.persistence.TransactionDAO
import ar.edu.unq.desapp.grupog.backenddesappapi.persistence.UserDAO
import ar.edu.unq.desapp.grupog.backenddesappapi.service.UserService
import ar.edu.unq.desapp.grupog.backenddesappapi.service.*
import ar.edu.unq.desapp.grupog.backenddesappapi.webservice.dtos.CryptoActiveDTO
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
    @Autowired private lateinit var transactionService: TransactionService
    @Autowired private lateinit var intentionService: IntentionService

    override fun getCryptoVolume(user: User, initialDate: LocalDateTime, finalDate: LocalDateTime) : CryptoVolume {
        val transactions =  transactionDAO.getFinishedTransactions().
                            filter { t -> t.user_whoCreate().id == user.id &&
                                    isBetweenDate(t.creationDate, initialDate, finalDate)
                            }
        val arsAmount = transactions.sumOf { t -> t.arsAmount!! }
        val usdAmount = arsAmount / 400
        val totalActives = transactions.map { t -> t.intention.getCryptoActive() }.toSet()
        val activesData = totalActives.map { active -> getActiveData(active, transactions) }

        return CryptoVolume(LocalDateTime.now(), usdAmount, arsAmount, activesData)
    }
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

    private fun isBetweenDate(date: LocalDateTime, initialDate: LocalDateTime, finalDate: LocalDateTime ) : Boolean {
        return date.isAfter(initialDate) && date.isBefore(finalDate)
    }

    private fun getActiveData(active: CryptoActiveName, transactions: List<Transaction>): CryptoActiveDTO {
        val transactionsWithActive = transactions.filter { t -> t.cryptoActive().toString() == active.name }
        val cryptoAmount = transactionsWithActive.sumOf { t -> t.cryptoAmount() }
        val arsPrice : Double = 1.0 * 400.0 * cryptoAmount
        return CryptoActiveDTO(active, cryptoAmount, 1.0, arsPrice)
    }
}
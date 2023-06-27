package ar.edu.unq.desapp.grupog.backenddesappapi.service.impl

import ar.edu.unq.desapp.grupog.backenddesappapi.model.*
import ar.edu.unq.desapp.grupog.backenddesappapi.model.exceptions.OutOfRangePriceException
import ar.edu.unq.desapp.grupog.backenddesappapi.model.exceptions.OverPriceException
import ar.edu.unq.desapp.grupog.backenddesappapi.model.exceptions.UnderPriceException
import ar.edu.unq.desapp.grupog.backenddesappapi.model.exceptions.UserAlreadyRegisteredException
import ar.edu.unq.desapp.grupog.backenddesappapi.model.trxHelpers.TrxType
import ar.edu.unq.desapp.grupog.backenddesappapi.persistence.TransactionDAO
import ar.edu.unq.desapp.grupog.backenddesappapi.persistence.UserDAO
import ar.edu.unq.desapp.grupog.backenddesappapi.service.UserService
import ar.edu.unq.desapp.grupog.backenddesappapi.service.*
import ar.edu.unq.desapp.grupog.backenddesappapi.webservice.dtos.CryptoActiveDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Transactional
@Service
class UserServiceImpl : UserService {

    @Autowired private lateinit var userDAO: UserDAO
    @Autowired private lateinit var transactionDAO: TransactionDAO
    @Autowired private lateinit var transactionService: TransactionService
    @Autowired private lateinit var intentionService: IntentionService

    @Autowired private lateinit var apisService: ExternalApisServiceImpl

    override fun getCryptoVolume(userId: Long, initialDate: LocalDateTime, finalDate: LocalDateTime) : CryptoVolume {
        if (!userDAO.existsById(userId))
            throw NoSuchElementException("The received ID doesn't match with any user in the database")
        val initial = LocalDateTime.of(initialDate.year, initialDate.month, initialDate.dayOfMonth, 0, 0)
        val final = LocalDateTime.of(finalDate.year, finalDate.month, finalDate.dayOfMonth, 0, 0)

        val transactions =  transactionDAO.getFinishedTransactions().
        filter { t -> t.user_whoCreate().id == userId &&
                                    isBetweenDate(t.creationDate, initial, final)
        }
        val arsAmount = transactions.sumOf { t -> t.arsAmount!! }
        val usdAmount = arsAmount / apisService.getDollarPrice()
        val totalActives = transactions.map { t -> t.intention.getCryptoActive() }.toSet()
        val activesData = totalActives.map { active -> getActiveData(active, transactions) }

        return CryptoVolume(LocalDateTime.now(), usdAmount, arsAmount, activesData)
    }
    override fun create(entity: User): User {
        if (userDAO.existsByEmail(entity.email)) throw UserAlreadyRegisteredException("email", entity.email)
        if (userDAO.existsByCVU(entity.cvu)) throw UserAlreadyRegisteredException("cvu", entity.cvu)
        if (userDAO.existsByWallet(entity.wallet)) throw UserAlreadyRegisteredException("wallet", entity.wallet)
        return userDAO.save(entity)
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

    override fun findByEmail(email: String): User {
        val daoResponse = userDAO.findByEmail(email)
        if (daoResponse.isPresent) return daoResponse.get()
        else throw NoSuchElementException("The received email doesn't match with any user in the database")
    }

    override fun readAll(): List<User> = userDAO.findAll().toList()

    override fun beginTransaction(userId: Long, intentionId: Long): Transaction {
        val user = read(userId)
        val intention = intentionService.read(intentionId)

        val transaction = transactionService.create(user.beginTransaction(intention, apisService.getCryptoPrice(intention.getCryptoActive())).apply {
            arsAmount = cryptoPrice * cryptoAmount() * apisService.getDollarPrice()
        })
        if (isUnderPrice(intention, transaction)) {
            transactionService.cancelTransaction(transaction.id!!)
            throw UnderPriceException()
        }
        else if (isOverPrice(intention, transaction)) {
            transactionService.cancelTransaction(transaction.id!!)
            throw OverPriceException()
        }

        return transaction
    }

    private fun isUnderPrice(
        intention: Intention,
        transaction: Transaction
    ) = intention.getTrxType() == TrxType.SELL && transaction.cryptoPrice < intention.getCryptoPrice()

    private fun isOverPrice(
        intention: Intention,
        transaction: Transaction
    ) = intention.getTrxType() == TrxType.BUY && transaction.cryptoPrice > intention.getCryptoPrice()

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
        val cryptoPrice = apisService.getCryptoPrice(active)
        val arsPrice : Double = cryptoPrice * apisService.getDollarPrice() * cryptoAmount
        return CryptoActiveDTO(active, cryptoAmount, cryptoPrice, arsPrice)
    }
}
package ar.edu.unq.desapp.grupog.backenddesappapi.service.impl

import ar.edu.unq.desapp.grupog.backenddesappapi.model.Transaction
import ar.edu.unq.desapp.grupog.backenddesappapi.persistence.TransactionDAO
import ar.edu.unq.desapp.grupog.backenddesappapi.service.TransactionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.RuntimeException

@Service
@Transactional
class TransactionServiceImpl : TransactionService {

    @Autowired private lateinit var transactionDAO: TransactionDAO

    override fun create(entity: Transaction): Transaction {
        return try {
            transactionDAO.save(entity)
        } catch (e: Exception) {
            throw RuntimeException(e.message)
        }
    }

    override fun update(entity: Transaction): Transaction {
        return if (transactionDAO.existsById(entity.id!!)) {
            transactionDAO.save(entity)
        } else { throw RuntimeException("The transaction to update does not exists") }
    }

    override fun read(entityId: Long): Transaction {
        val daoResponse = transactionDAO.findById(entityId)
        if (daoResponse.isPresent) {
            val trx = daoResponse.get()
            trx.stateBehavior = trx.status.behavior()
            return trx
        }
        else throw RuntimeException("The received ID doesn't match with any transaction in the database")
    }

    override fun cancelTransaction(transactionId: Long): Transaction {
        val transaction = read(transactionId)

        transaction.cancelByMaybeUser(null)
        return transactionDAO.save(transaction)
    }

    override fun readAll(): List<Transaction> = transactionDAO.findAll().toList()

    override fun delete(entityId: Long) { transactionDAO.deleteById(entityId) }

    override fun deleteAll() { transactionDAO.deleteAll() }
}
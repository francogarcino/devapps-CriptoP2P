package ar.edu.unq.desapp.grupog.backenddesappapi.persistence

import ar.edu.unq.desapp.grupog.backenddesappapi.model.Transaction
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface TransactionDAO : JpaRepository<Transaction, Long> {

    @Query(
            """
                FROM Transaction t WHERE t.status = "DONE"
            """
    )
    fun getFinishedTransactions(): List<Transaction>

}
package ar.edu.unq.desapp.grupog.backenddesappapi.persistence

import ar.edu.unq.desapp.grupog.backenddesappapi.model.Transaction
import org.springframework.data.jpa.repository.JpaRepository

interface TransactionDAO : JpaRepository<Transaction, Long> {
}
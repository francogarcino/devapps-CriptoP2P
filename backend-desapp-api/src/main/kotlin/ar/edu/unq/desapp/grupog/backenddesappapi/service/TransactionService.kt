package ar.edu.unq.desapp.grupog.backenddesappapi.service

import ar.edu.unq.desapp.grupog.backenddesappapi.model.Transaction

interface TransactionService: CrudService<Transaction> {
    fun cancelTransaction(transactionId: Long): Transaction
}
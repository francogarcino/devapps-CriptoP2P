package ar.edu.unq.desapp.grupog.backenddesappapi.service

import ar.edu.unq.desapp.grupog.backenddesappapi.model.Transaction
import ar.edu.unq.desapp.grupog.backenddesappapi.model.User

interface UserService : CrudService<User>{
    fun beginTransaction(userId: Long, intentionId: Long): Transaction
    fun registerTransfer(userId: Long, transactionId: Long): Transaction
    fun registerReleaseCrypto(userId: Long, transactionId: Long): Transaction
}
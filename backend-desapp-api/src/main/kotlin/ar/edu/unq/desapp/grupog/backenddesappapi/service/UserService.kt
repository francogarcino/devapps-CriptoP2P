package ar.edu.unq.desapp.grupog.backenddesappapi.service

import ar.edu.unq.desapp.grupog.backenddesappapi.model.Transaction
import ar.edu.unq.desapp.grupog.backenddesappapi.model.User

interface UserService : CrudService<User>{
    fun beginTransaction(userId: Long, intentionId: Long): Transaction
    fun registerTransfer(transactionId: Long, userId: Long): Transaction
}
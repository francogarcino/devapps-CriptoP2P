package ar.edu.unq.desapp.grupog.backenddesappapi.webservice.mappers

import ar.edu.unq.desapp.grupog.backenddesappapi.model.Transaction
import ar.edu.unq.desapp.grupog.backenddesappapi.webservice.dtos.TransactionDTO

class TransactionMapper {

    private val userMapper = UserMapper()

    fun fromTransactionToDTO(transaction: Transaction) : TransactionDTO {
        val userWhoCreateDTO = userMapper.fromUserToDTO(transaction.user_whoCreate())
        val userWhoAcceptDTO = userMapper.fromUserToDTO(transaction.user_whoAccept)
        return TransactionDTO(transaction.id,
                transaction.cryptoActive(),
                transaction.cryptoAmount(),
                transaction.cryptoPrice(),
                transaction.arsAmount!!,
                userWhoCreateDTO,
                userWhoAcceptDTO,
                transaction.typeTransaction(),
                transaction.status)
    }

}
package ar.edu.unq.desapp.grupog.backenddesappapi.webservice.dtos

import ar.edu.unq.desapp.grupog.backenddesappapi.model.CryptoActiveName
import ar.edu.unq.desapp.grupog.backenddesappapi.model.trxHelpers.TrxStatus
import ar.edu.unq.desapp.grupog.backenddesappapi.model.trxHelpers.TrxType

class TransactionDTO(
    var id: Long?,
    var cryptoActive: CryptoActiveName,
    var cryptoAmount: Int,
    var cryptoPrice: Double,
    var arsAmount: Double,
    var userWhoCreate: UserDTO,
    var userWhoAccept: UserDTO,
    var trxType: TrxType,
    var trxStatus: TrxStatus
)
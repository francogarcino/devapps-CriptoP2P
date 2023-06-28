package ar.edu.unq.desapp.grupog.backenddesappapi.webservice.dtos

import ar.edu.unq.desapp.grupog.backenddesappapi.model.CryptoActiveName
import ar.edu.unq.desapp.grupog.backenddesappapi.model.trxHelpers.TrxType
import java.time.LocalDateTime

class IntentionDTO(
    var id: Long,
    var cryptoActive: CryptoActiveName,
    var cryptoAmount: Int,
    var cryptoPrice: Double,
    var arsAmount: Double,
    var user: UserDTO,
    var trxType: TrxType,
    var date: LocalDateTime,
    var available: Boolean
)
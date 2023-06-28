package ar.edu.unq.desapp.grupog.backenddesappapi.webservice.dtos

import ar.edu.unq.desapp.grupog.backenddesappapi.model.CryptoActiveName
import ar.edu.unq.desapp.grupog.backenddesappapi.model.trxHelpers.TrxType

class IntentionCreateDTO (
    var cryptoActive: CryptoActiveName,
    var cryptoAmount: Int,
    var cryptoPrice: Double,
    var trxType: TrxType
)
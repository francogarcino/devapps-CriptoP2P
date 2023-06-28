package ar.edu.unq.desapp.grupog.backenddesappapi.test.utils

import ar.edu.unq.desapp.grupog.backenddesappapi.model.CryptoActiveName
import ar.edu.unq.desapp.grupog.backenddesappapi.model.trxHelpers.TrxType
import ar.edu.unq.desapp.grupog.backenddesappapi.webservice.dtos.IntentionCreateDTO

class IntentionCreateDTOBuilder {
    private var cryptoActive = CryptoActiveName.ALICEUSDT
    private var cryptoAmount = 10
    private var cryptoPrice = 30.5
    private var trxType = TrxType.BUY

    fun build() = IntentionCreateDTO(cryptoActive, cryptoAmount, cryptoPrice, trxType)

    fun withCryptoActive(newCryptoActive: CryptoActiveName) = this.apply { cryptoActive = newCryptoActive }
    fun withCryptoAmount(newCryptoAmount: Int) = this.apply { cryptoAmount = newCryptoAmount }
    fun withCryptoPrice(newCryptoPrice: Double) = this.apply { cryptoPrice = newCryptoPrice }
    fun withTrxType(newTrxType: TrxType) = this.apply { trxType = newTrxType }
}
package ar.edu.unq.desapp.grupog.backenddesappapi.test.utils

import ar.edu.unq.desapp.grupog.backenddesappapi.model.*
import ar.edu.unq.desapp.grupog.backenddesappapi.model.trxHelpers.TrxType
import java.time.LocalDateTime

class IntentionBuilder {
    private var cryptoActive = CryptoActiveName.ALICEUSDT
    private var cryptoAmount = 10
    private var cryptoPrice = 30.5
    private var user = UserBuilder().build()
    private var trxType = TrxType.BUY
    private var date = LocalDateTime.of(2023, 3, 15, 11, 20, 53, 10)

    fun build() = Intention(cryptoActive, cryptoAmount, cryptoPrice, user, trxType, date)

    fun withCryptoActive(newCryptoActive: CryptoActiveName) = this.apply { cryptoActive = newCryptoActive }
    fun withCryptoAmount(newCryptoAmount: Int) = this.apply { cryptoAmount = newCryptoAmount }
    fun withCryptoPrice(newCryptoPrice: Double) = this.apply { cryptoPrice = newCryptoPrice }
    fun withUser(newUser: User) = this.apply { user = newUser }
    fun withTrxType(newTrxType: TrxType) = this.apply { trxType = newTrxType }
    fun withDate(newDate: LocalDateTime) = this.apply { date = newDate }
}
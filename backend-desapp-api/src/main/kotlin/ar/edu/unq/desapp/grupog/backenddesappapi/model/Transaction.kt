package ar.edu.unq.desapp.grupog.backenddesappapi.model

import ar.edu.unq.desapp.grupog.backenddesappapi.model.trxHelpers.TrxStatus
import ar.edu.unq.desapp.grupog.backenddesappapi.model.trxHelpers.TrxType

class Transaction(
        var intention: Intention,
        var user1: User
) {
    var status : TrxStatus = TrxStatus.WAITING
    lateinit var cryptoActive: CryptoActiveName
    var cryptoAmount: Int? = null
    var arsAmount: Double? = null
    lateinit var typeTransaction: TrxType
    lateinit var user2: User
    lateinit var address: String
    var cryptoPrice: Double = 0.0

    fun cancelledByUser(user: User) {
//       user.discountPionts(20)
    }
    fun setInformation() {
        cryptoActive = intention.cryptoActive
        cryptoAmount = intention.cryptoAmount
        arsAmount = intention.arsAmount
        typeTransaction = intention.trxType
        user2 = intention.user
        cryptoPrice = intention.cryptoPrice
        address = if(typeTransaction == TrxType.SELL) user1.cvu
        else user1.wallet
    }

    // Si la trx es cancelada por un usuario -> descontar 20 pts de reputacion
    // Si la trx es cancelada por el sistema -> nada
    // Si la trx es realizada en 30 min -> sumar 10 puntos
    // Si la trx es realizada en + 30 min -> sumar 5 pts
    init {
        setInformation()
    }
}
package ar.edu.unq.desapp.grupog.backenddesappapi.model

import ar.edu.unq.desapp.grupog.backenddesappapi.model.trxHelpers.TrxType

class Transaction(
        var cryptoActive: CryptoActive,
        var cryptoAmount: Int,
        var arsAmount: Double,
        var user1: User,
        var user2: User,
        var typeTransacstion: TrxType
) {
    lateinit var address: String
    var cryptoPrice: Double = 0.0
    // cantidad de operaciones realizadas por el usuario

    fun setAddress() {
        address = if(typeTransaction == TrxType.SELL) user1.cvu
        else user1.wallet
    }

    fun setCryptoPrice() { cryptoPrice = cryptoActive.value }

    // Si la trx es cancelada por un usuario -> descontar 20 pts de reputacion
    // Si la trx es cancelada por el sistema -> nada
    // Si la trx es realizada en 30 min -> sumar 10 puntos
    // Si la trx es realizada en + 30 min -> sumar 5 pts
    init {
        setAddress()
        setCryptoPrice()
    }
}
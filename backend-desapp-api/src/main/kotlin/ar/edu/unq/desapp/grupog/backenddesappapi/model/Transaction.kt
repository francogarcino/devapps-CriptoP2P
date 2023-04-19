package ar.edu.unq.desapp.grupog.backenddesappapi.model

import ar.edu.unq.desapp.grupog.backenddesappapi.model.trxHelpers.TrxStateClasses.TrxStateBehaivour
import ar.edu.unq.desapp.grupog.backenddesappapi.model.trxHelpers.TrxStateClasses.WaitingBehaivour
import ar.edu.unq.desapp.grupog.backenddesappapi.model.trxHelpers.TrxStatus
import ar.edu.unq.desapp.grupog.backenddesappapi.model.trxHelpers.TrxType

class Transaction(
        var intention: Intention,
        var user1: User
) {
    // Solo para persistencia, capaz lo omitimos?
    var status : TrxStatus = TrxStatus.WAITING
    var stateBehaivour : TrxStateBehaivour = WaitingBehaivour()

    // Referenciar unicamente la intention para evitar redundancia de datos?
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
        cryptoActive = intention.getCryptoActive()
        cryptoPrice = intention.getCryptoPrice()
        cryptoAmount = intention.getCryptoAmount()
        // A futuro, el 400 sera el valor retornado por la api al consultar el precio del dolar + cryptoPrice sera consumido desde la API de Binance
        arsAmount = cryptoAmount!! * 400 * cryptoPrice
        typeTransaction = intention.getTrxType()
        user2 = intention.getUser()

        address = if(typeTransaction == TrxType.SELL) user2.cvu
            else user1.cvu
    }

    // Si la trx es cancelada por un usuario -> descontar 20 pts de reputacion
    // Si la trx es cancelada por el sistema -> nada
    // Si la trx es realizada en 30 min -> sumar 10 puntos
    // Si la trx es realizada en + 30 min -> sumar 5 pts

    init {
        setInformation()
    }
}
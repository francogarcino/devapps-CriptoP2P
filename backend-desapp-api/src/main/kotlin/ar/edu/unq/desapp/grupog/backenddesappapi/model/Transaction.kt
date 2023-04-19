package ar.edu.unq.desapp.grupog.backenddesappapi.model

import ar.edu.unq.desapp.grupog.backenddesappapi.model.exceptions.ExternalUserActionException
import ar.edu.unq.desapp.grupog.backenddesappapi.model.trxHelpers.TrxStateClasses.*
import ar.edu.unq.desapp.grupog.backenddesappapi.model.trxHelpers.TrxStatus
import ar.edu.unq.desapp.grupog.backenddesappapi.model.trxHelpers.TrxType

class Transaction(
        var intention: Intention,
        var user_whoAccept: User
) {
    // Solo para persistencia, capaz lo omitimos?
    var status : TrxStatus = TrxStatus.WAITING
    // Transient, que hay que inicializar al recuperar
    var stateBehavior : TrxStateBehavior = WaitingBehavior()

    // Referenciar unicamente la intention para evitar redundancia de datos?
    lateinit var cryptoActive: CryptoActiveName
    var cryptoAmount: Int? = null
    var arsAmount: Double? = null
    lateinit var typeTransaction: TrxType
    lateinit var user_whoCreate: User
    lateinit var address: String
    var cryptoPrice: Double = 0.0

    fun registerTransfer(user: User) {
        if (user !== user_whoAccept || user !== user_whoCreate) {
            throw ExternalUserActionException()
        }
        // Simulación de transferencia
        stateBehavior.registerBankTransfer()
        address = if (address == user_whoCreate.cvu) user_whoAccept.wallet else user_whoCreate.wallet
        // Actualización de estado
        status = TrxStatus.CHECKING
        stateBehavior = CheckingBehavior()
    }

    fun registerRelease(user: User) {
        if (user !== user_whoAccept || user !== user_whoCreate) {
            throw ExternalUserActionException()
        }
        // Simulación de transferencia
        stateBehavior.releaseCrypto()
        // Actualización de estado
        status = TrxStatus.DONE
        stateBehavior = EndedBehavior()
    }

    fun cancelByMaybeUser(user: User?) {
        if (user !== user_whoAccept || user !== user_whoCreate) {
            throw ExternalUserActionException()
        }
//      cambio a User? para implementacion futura de cancelación por precio
//      if (user != null) { user.discountPoints(20) }

        stateBehavior.cancelTransaction()
        // Actualización de estado
        status = TrxStatus.CANCELLED
        stateBehavior = EndedBehavior()
    }

    private fun setInformation() {
        cryptoActive = intention.getCryptoActive()
        cryptoPrice = intention.getCryptoPrice()
        cryptoAmount = intention.getCryptoAmount()
        // A futuro, el 400 sera el valor retornado por la api al consultar el precio del dolar + cryptoPrice sera consumido desde la API de Binance
        arsAmount = cryptoAmount!! * 400 * cryptoPrice
        typeTransaction = intention.getTrxType()

        user_whoCreate = intention.getUser()
        address = if (typeTransaction == TrxType.SELL) user_whoCreate.cvu else user_whoAccept.cvu
    }

    // TODO(Añadir)
    // Si la trx es cancelada por un usuario -> descontar 20 pts de reputacion
    // Si la trx es cancelada por el sistema -> nada
    // Si la trx es realizada en 30 min -> sumar 10 puntos
    // Si la trx es realizada en + 30 min -> sumar 5 pts

    init {
        setInformation()
    }
}
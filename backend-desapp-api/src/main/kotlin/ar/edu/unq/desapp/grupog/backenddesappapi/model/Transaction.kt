package ar.edu.unq.desapp.grupog.backenddesappapi.model

import ar.edu.unq.desapp.grupog.backenddesappapi.model.exceptions.ExternalUserActionException
import ar.edu.unq.desapp.grupog.backenddesappapi.model.trxHelpers.TrxStateClasses.*
import ar.edu.unq.desapp.grupog.backenddesappapi.model.trxHelpers.TrxStatus
import ar.edu.unq.desapp.grupog.backenddesappapi.model.trxHelpers.TrxType
import jakarta.persistence.*

@Entity
class Transaction(
    @ManyToOne
    var intention: Intention,
    @ManyToOne
    var user_whoAccept: User
) {
    // Solo para persistencia, capaz lo omitimos?
    @Enumerated(EnumType.STRING)
    var status: TrxStatus = TrxStatus.WAITING
    // Transient, que hay que inicializar al recuperar
    @Transient
    var stateBehavior: TrxStateBehavior = WaitingBehavior()

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    var arsAmount: Double? = null
    fun cryptoActive() = intention.getCryptoActive()
    fun cryptoAmount() = intention.getCryptoAmount()
    fun typeTransaction() = intention.getTrxType()
    fun user_whoCreate() = intention.getUserFromIntention()
    fun cryptoPrice() = intention.getCryptoPrice()

    fun registerTransfer(user: User) {
        if (user !== user_whoAccept && user !== user_whoCreate()) {
            throw ExternalUserActionException()
        }
        // Simulación de transferencia
        stateBehavior.registerBankTransfer()
        // Actualización de estado
        status = TrxStatus.CHECKING
        stateBehavior = CheckingBehavior()
    }

    fun registerRelease(user: User) {
        if (user !== user_whoAccept && user !== user_whoCreate()) {
            throw ExternalUserActionException()
        }
        // Simulación de transferencia
        stateBehavior.releaseCrypto()
        // Actualización de estado
        status = TrxStatus.DONE
        stateBehavior = EndedBehavior()
    }

    fun cancelByMaybeUser(user: User?) {
        if (user !== user_whoAccept && user !== user_whoCreate()) {
            throw ExternalUserActionException()
        }
        // if (user != null) { user.discountPoints(20) }

        stateBehavior.cancelTransaction()

        status = TrxStatus.CANCELLED
        stateBehavior = EndedBehavior()
    }

    fun address(): String {
        return when (status) {
            TrxStatus.WAITING -> { chooseCvuToShow() }
            TrxStatus.CHECKING -> { chooseWalletToShow() }
            else -> "Transaction Completed or Finished, no needed address right now"
        }
    }

    private fun chooseCvuToShow() = when (intention.getTrxType()) {
        TrxType.BUY -> user_whoAccept.cvu
        else -> user_whoCreate().cvu
    }

    private fun chooseWalletToShow() = when (intention.getTrxType()) {
        TrxType.BUY -> user_whoCreate().wallet
        else -> user_whoAccept.wallet
    }

    private fun setInformation() {
        // A futuro, el 400 será el valor retornado por la api al consultar el precio del dólar + cryptoPrice será consumido desde la API de Binance
        arsAmount = cryptoAmount() * 400 * cryptoPrice()
    }

    /*
        Si la trx es cancelada por un usuario → descontar 20 pts de reputación
        Si la trx es cancelada por el sistema → nada
        Si la trx es realizada en 30 min → sumar 10 puntos
        Si la trx es realizada en + 30 min → sumar 5 pts
     */

    init {
        setInformation()
    }
}
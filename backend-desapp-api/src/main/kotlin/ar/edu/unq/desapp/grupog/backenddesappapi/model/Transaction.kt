package ar.edu.unq.desapp.grupog.backenddesappapi.model

import ar.edu.unq.desapp.grupog.backenddesappapi.model.exceptions.ExternalUserActionException
import ar.edu.unq.desapp.grupog.backenddesappapi.model.exceptions.trx.UnableActionException
import ar.edu.unq.desapp.grupog.backenddesappapi.model.trxHelpers.TrxStateClasses.*
import ar.edu.unq.desapp.grupog.backenddesappapi.model.trxHelpers.TrxStatus
import ar.edu.unq.desapp.grupog.backenddesappapi.model.trxHelpers.TrxType
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class Transaction(
    @ManyToOne
    var intention: Intention,
    @ManyToOne
    var user_whoAccept: User,
    var cryptoPrice : Double
) {
    @Enumerated(EnumType.STRING)
    var status: TrxStatus = TrxStatus.WAITING
    @Transient
    var stateBehavior: TrxStateBehavior = WaitingBehavior()

    @Column(nullable = false) var creationDate: LocalDateTime = LocalDateTime.now()

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    var arsAmount: Double? = null
    fun cryptoActive() = intention.getCryptoActive()
    fun cryptoAmount() = intention.getCryptoAmount()
    fun typeTransaction() = intention.getTrxType()
    fun user_whoCreate() = intention.getUserFromIntention()

    fun registerTransfer(user: User) {
        // Si es compra, quien debe pagar en ARS es el user_whoCreate
        // Si es venta, espero que quien pague sea el user_whoAccept
        if (user != user_whoAccept && user != user_whoCreate()) throw ExternalUserActionException()
        if ((user == user_whoAccept && intention.getTrxType() == TrxType.BUY) || (user == user_whoCreate() && intention.getTrxType() == TrxType.SELL)) {
            throw UnableActionException()
        }
        // Simulación de transferencia
        stateBehavior.registerBankTransfer()
        // Actualización de estado
        status = TrxStatus.CHECKING
        stateBehavior = CheckingBehavior()
    }

    fun registerRelease(user: User) {
        // Si es compra, quien debe liberar cryptos es el user_whoAccept
        // Si es venta, espero que quien libere sea el user_whoCreate
        if (user != user_whoAccept && user != user_whoCreate()) throw ExternalUserActionException()
        if ((user == user_whoCreate() && intention.getTrxType() == TrxType.BUY) || (user == user_whoAccept && intention.getTrxType() == TrxType.SELL)) {
            throw UnableActionException()
        }
        // Simulación de transferencia
        stateBehavior.releaseCrypto()

        val finishTime = creationDate.plusMinutes(30)
        if (finishTime.isAfter(LocalDateTime.now())) increaseReputationToBothUsersBy(10) else increaseReputationToBothUsersBy(5)

        // Actualización de estado
        status = TrxStatus.DONE
        stateBehavior = EndedBehavior()
    }

    private fun increaseReputationToBothUsersBy(amount: Int) {
        user_whoAccept.increaseReputation(amount)
        user_whoCreate().increaseReputation(amount)
    }

    fun cancelByMaybeUser(user: User?) {
        if (user != null && user != user_whoAccept && user != user_whoCreate()) {
            throw ExternalUserActionException()
        }
        user?.discountReputation(20)

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

}
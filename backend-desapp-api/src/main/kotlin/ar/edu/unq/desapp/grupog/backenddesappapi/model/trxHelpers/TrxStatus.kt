package ar.edu.unq.desapp.grupog.backenddesappapi.model.trxHelpers

import ar.edu.unq.desapp.grupog.backenddesappapi.model.trxHelpers.TrxStateClasses.CheckingBehavior
import ar.edu.unq.desapp.grupog.backenddesappapi.model.trxHelpers.TrxStateClasses.EndedBehavior
import ar.edu.unq.desapp.grupog.backenddesappapi.model.trxHelpers.TrxStateClasses.TrxStateBehavior
import ar.edu.unq.desapp.grupog.backenddesappapi.model.trxHelpers.TrxStateClasses.WaitingBehavior

enum class TrxStatus {
    WAITING { override fun behavior(): TrxStateBehavior = WaitingBehavior() },
    CHECKING { override fun behavior(): TrxStateBehavior = CheckingBehavior() },
    CANCELLED { override fun behavior(): TrxStateBehavior = EndedBehavior() },
    DONE { override fun behavior(): TrxStateBehavior = EndedBehavior() };

    abstract fun behavior() : TrxStateBehavior
}
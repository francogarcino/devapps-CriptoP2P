package ar.edu.unq.desapp.grupog.backenddesappapi.model.trxHelpers.TrxStateClasses

import ar.edu.unq.desapp.grupog.backenddesappapi.model.exceptions.trx.ActionOnEndedTransactionException

class EndedBehavior : TrxStateBehavior() {
    override fun registerBankTransfer() { throw ActionOnEndedTransactionException() }
    override fun releaseCrypto() { throw ActionOnEndedTransactionException() }
    override fun cancelTransaction() { throw ActionOnEndedTransactionException() }
}

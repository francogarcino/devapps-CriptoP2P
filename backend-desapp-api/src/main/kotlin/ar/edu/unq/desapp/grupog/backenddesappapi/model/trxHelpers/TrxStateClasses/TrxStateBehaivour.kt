package ar.edu.unq.desapp.grupog.backenddesappapi.model.trxHelpers.TrxStateClasses

import ar.edu.unq.desapp.grupog.backenddesappapi.model.exceptions.trx.UnableActionException

abstract class TrxStateBehaivour {
    // En cada estado, solo una acción funciona correctamente, a excepción de cancelar, que posee comportamiento especial
    fun registerBankTransfer() { throw UnableActionException() }
    fun releaseCrypto() { throw UnableActionException() }
    fun completeTransaction() { throw UnableActionException() }

    abstract fun cancelTransaction()
    abstract fun updateState()
}
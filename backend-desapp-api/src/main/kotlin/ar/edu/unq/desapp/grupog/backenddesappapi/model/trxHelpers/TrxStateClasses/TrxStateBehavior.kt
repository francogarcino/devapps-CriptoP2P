package ar.edu.unq.desapp.grupog.backenddesappapi.model.trxHelpers.TrxStateClasses

import ar.edu.unq.desapp.grupog.backenddesappapi.model.exceptions.trx.UnableActionException

abstract class TrxStateBehavior {
    // En cada estado, solo una acción funciona correctamente, a excepción de cancelar, que posee comportamiento especial
    open fun registerBankTransfer() { throw UnableActionException() }
    open fun releaseCrypto() { throw UnableActionException() }
    open fun cancelTransaction() { /* hook method */ }
}
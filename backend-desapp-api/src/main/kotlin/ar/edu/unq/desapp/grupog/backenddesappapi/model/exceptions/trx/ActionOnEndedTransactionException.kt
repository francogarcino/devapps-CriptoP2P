package ar.edu.unq.desapp.grupog.backenddesappapi.model.exceptions.trx

class ActionOnEndedTransactionException : Throwable() {
    override val message: String
        get() = "This transaction is done or cancelled, so you can't operate with it"
}

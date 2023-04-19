package ar.edu.unq.desapp.grupog.backenddesappapi.model.exceptions

class ExternalUserActionException : Throwable() {
    override val message: String
        get() = "The user received is not an user involved in this transaction"
}

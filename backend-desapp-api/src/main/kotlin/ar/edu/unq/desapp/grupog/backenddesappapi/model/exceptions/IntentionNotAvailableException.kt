package ar.edu.unq.desapp.grupog.backenddesappapi.model.exceptions

class IntentionNotAvailableException : Throwable() {
    override val message: String
        get() = "The intention is not available"
}
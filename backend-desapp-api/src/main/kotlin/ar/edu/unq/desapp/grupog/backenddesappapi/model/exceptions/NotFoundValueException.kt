package ar.edu.unq.desapp.grupog.backenddesappapi.model.exceptions

class NotFoundValueException : Throwable() {
    override val message: String
        get() = "The value searched was not founded"
}

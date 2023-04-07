package ar.edu.unq.desapp.grupog.backenddesappapi.model.exceptions

class InvalidEmailException : Throwable() {
    override val message: String
        get() = "The email used is not valid. Try with a valid one."
}

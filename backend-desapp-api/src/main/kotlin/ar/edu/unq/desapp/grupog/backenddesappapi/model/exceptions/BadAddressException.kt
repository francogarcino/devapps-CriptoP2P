package ar.edu.unq.desapp.grupog.backenddesappapi.model.exceptions

class BadAddressException : Throwable() {
    override val message: String
        get() = "Address must have between 10 and 30 characters"
}

package ar.edu.unq.desapp.grupog.backenddesappapi.model.exceptions

class InvalidNameAttempException : Throwable() {
    override val message: String
        get() = "The first or last name doesn't match with the expected format."
}

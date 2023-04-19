package ar.edu.unq.desapp.grupog.backenddesappapi.model.exceptions

class SameUserException : Throwable() {
    override val message: String
        get() = "The user who creates the intention and the user who accepts cannot be the same"
}

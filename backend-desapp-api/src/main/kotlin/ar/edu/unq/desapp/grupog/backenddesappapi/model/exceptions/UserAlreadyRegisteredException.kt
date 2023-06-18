package ar.edu.unq.desapp.grupog.backenddesappapi.model.exceptions

class UserAlreadyRegisteredException(private val entity: String, private val value: String) : Throwable() {
    override val message: String
        get() = "The user with $entity $value is already registered"
}
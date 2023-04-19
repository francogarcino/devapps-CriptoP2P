package ar.edu.unq.desapp.grupog.backenddesappapi.model.exceptions

class BadBankDataException : Throwable() {
    override val message: String
        get() = "The CVU or Wallet address is invalid. Make sure that CVU address has 22 characters and Wallet address has 8"
}

package ar.edu.unq.desapp.grupog.backenddesappapi.model.exceptions

class UnderPriceException : Throwable() {
    override val message: String
        get() = "At this moment, the crypto value is lower than the expected price. Try again later or use other intention"
}

package ar.edu.unq.desapp.grupog.backenddesappapi.model.exceptions

class OverPriceException : Throwable() {
    override val message: String
        get() = "At this moment, the crypto value is higher than the expected price. Try again later or use other intention"
}

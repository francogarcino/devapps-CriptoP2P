package ar.edu.unq.desapp.grupog.backenddesappapi.model.exceptions

class OutOfRangePriceException : Throwable() {
    override val message: String
        get() = "The price given is not valid. You should enter a price that is 5 percent higher or lower than the current price."
}

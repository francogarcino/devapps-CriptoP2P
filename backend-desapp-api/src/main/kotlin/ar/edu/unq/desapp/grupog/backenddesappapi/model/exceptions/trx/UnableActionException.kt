package ar.edu.unq.desapp.grupog.backenddesappapi.model.exceptions.trx

class UnableActionException : Throwable() {
    override val message: String
        get() = """
            You have tried to execute an action when it is not supposed to be executed. 
            Please wait until the other user makes their move or the transaction ends.
        """.trimIndent()
}

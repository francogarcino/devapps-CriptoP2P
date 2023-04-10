package ar.edu.unq.desapp.grupog.backenddesappapi.model.exceptions

class InvalidPasswordException : Throwable() {
    override val message: String
        get() = """
            The password doesn't match with the expected format. Try again and remember these rules:
            >> The password must have 6 character at least
            >> The password must contains one capital letter, one small letter and one special character at least
        """.trimIndent()
}

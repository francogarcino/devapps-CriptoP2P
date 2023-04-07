package ar.edu.unq.desapp.grupog.backenddesappapi.model

import ar.edu.unq.desapp.grupog.backenddesappapi.model.exceptions.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class User(
    firstName: String,
    lastName: String,
    email: String,
    address: String,
    password: String,
    cvu: String,
    wallet: String
) {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null

    init {
        if (!isValidName(firstName) && !isValidName(lastName)) { throw InvalidNameAttempException() }
        if (!signupWithValidEmail(email)) { throw InvalidEmailException() }
        if (!isValidPassword(password)) { throw InvalidPasswordException() }
        if (!isValidBankData(cvu, 22) && !isValidBankData(wallet, 8)) { throw BadBankDataException() }
        if (!IntRange(10, 30).contains(address.length)) { throw BadAddressException() }
    }

    private fun isValidName(name: String): Boolean {
        return (IntRange(3, 30).contains(name.length))
    }
    private fun signupWithValidEmail(email: String): Boolean {
        val regex = Regex("""^[A-Za-z0-9._%+-]+@(gmail\.com|hotmail\.com)$""")
        return regex.matches(email)
    }
    private fun isValidPassword(password: String): Boolean {
        val regex = Regex("""^(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#$%^&*()_+\-=])$""")
        return regex.matches(password) && (password.length >= 6)
    }
    private fun isValidBankData(bankAddress: String, expectedLength: Int): Boolean {
        val regex = Regex("""^[0-9]$""")
        return regex.matches(bankAddress) && expectedLength == bankAddress.length
    }

}
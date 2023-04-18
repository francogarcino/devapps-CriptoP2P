package ar.edu.unq.desapp.grupog.backenddesappapi.model

import ar.edu.unq.desapp.grupog.backenddesappapi.model.exceptions.*
import ar.edu.unq.desapp.grupog.backenddesappapi.model.trxHelpers.TrxType
import jakarta.persistence.*

@Entity
class User(
    @Column(nullable = false) var firstName: String,
    @Column(nullable = false) var lastName: String,
    @Column(nullable = false, unique = true) var email: String,
    @Column(nullable = false) var address: String,
    @Column(nullable = false) var password: String,
    @Column(nullable = false, unique = true) var cvu: String,
    @Column(nullable = false, unique = true) var wallet: String
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @OneToMany
    var intentions: MutableSet<Intention> = mutableSetOf()

// fun createIntention

    fun cancelTransaction(transaction: Transaction) {
        transaction.cancelledByUser(this)
    }

    fun beginTransaction(intention: Intention) : Transaction {
        return Transaction(intention, this)
    }

//    fun discountPoints(amount : Int)

    init { validateUserData() }

    private fun validateUserData() {
        if (!isValidName(this.firstName) || !isValidName(this.lastName)) { throw InvalidNameAttempException() }
        if (!isValidEmail(this.email)) { throw InvalidEmailException() }
        if (!isValidPassword(this.password)) { throw InvalidPasswordException() }
        if (!isValidBankData(this.cvu, 22) || !isValidBankData(this.wallet, 8)) { throw BadBankDataException() }
        if (!isValidAddress()) { throw BadAddressException() }
    }

    private fun isValidName(name: String): Boolean {
        return (IntRange(3, 30).contains(name.length)) && name.isNotBlank()
    }
    private fun isValidEmail(email: String): Boolean {
        val regex = Regex("""^[A-Za-z0-9._%+-]+@(gmail\.com|hotmail\.com)$""")
        return regex.matches(email)
    }
    private fun isValidPassword(password: String): Boolean {
        val regex = Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*[\\d\\-_.]).{6,}\$")
        return regex.matches(password)
    }
    private fun isValidBankData(bankAddress: String, expectedLength: Int): Boolean {
        val regex = Regex("^[0-9]+$")
        return regex.matches(bankAddress) && expectedLength == bankAddress.length
    }
    private fun isValidAddress() = IntRange(10, 30).contains(this.address.length) && this.address.isNotBlank()
}
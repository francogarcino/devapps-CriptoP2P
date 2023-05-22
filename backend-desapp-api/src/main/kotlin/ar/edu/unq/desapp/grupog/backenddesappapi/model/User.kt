package ar.edu.unq.desapp.grupog.backenddesappapi.model

import ar.edu.unq.desapp.grupog.backenddesappapi.model.exceptions.*
import ar.edu.unq.desapp.grupog.backenddesappapi.model.trxHelpers.TrxType
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity(name = "UserApp")
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

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var intentions: MutableSet<Intention> = mutableSetOf()

    @OneToMany(mappedBy = "user_whoAccept", cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var transactions = mutableSetOf<Transaction>()

    fun createIntention(cryptoActive: CryptoActiveName, cryptoAmount: Int, cryptoPrice: Double, trxType: TrxType) : Intention {
        val intention = Intention(cryptoActive, cryptoAmount, cryptoPrice, this, trxType, LocalDateTime.now())
        intentions.add(intention)
        return intention
    }

    fun beginTransaction(intention: Intention) : Transaction {
        if(intention.getUserFromIntention() == this) throw SameUserException()
        return Transaction(intention, this)
    }

    fun transferMoneyToBankAccount(transaction: Transaction) {
        transaction.registerTransfer(this)
    }

    fun releaseCrypto(transaction: Transaction) {
        transaction.registerRelease(this)
    }

    fun cancelTransaction(transaction: Transaction) {
        transaction.cancelByMaybeUser(this)
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
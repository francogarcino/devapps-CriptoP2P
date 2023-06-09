package ar.edu.unq.desapp.grupog.backenddesappapi.model

import ar.edu.unq.desapp.grupog.backenddesappapi.model.trxHelpers.TrxType
import jakarta.persistence.*
import java.time.LocalDateTime
import javax.management.InvalidAttributeValueException

@Entity
class Intention(
    @Column(nullable = false) private var cryptoActive: CryptoActiveName,
    @Column(nullable = false) private var cryptoAmount: Int,
    @Column(nullable = false) private var cryptoPrice: Double,
    @ManyToOne var user: User,
    @Enumerated(EnumType.STRING) @Column(nullable = false) private var trxType: TrxType,
    @Column(nullable = false) private var date: LocalDateTime,
  ) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var id: Long? = null

    @OneToMany(mappedBy = "intention", cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    val transactions = mutableSetOf<Transaction>()

    // A futuro, el 400 será el valor retornado por la api al consultar el precio del dólar
    @Column(nullable = false) private var arsAmount: Double = cryptoAmount * 400 * cryptoPrice

    @Column(nullable = false)
    var available: Boolean = true

    fun getCryptoActive() = cryptoActive
    fun getCryptoAmount() = cryptoAmount
    fun getCryptoPrice() = cryptoPrice
    fun getArsAmount() = arsAmount
    fun getUserFromIntention() = user
    fun getTrxType() = trxType
    fun getDate() = date
    fun getId() = id

    init { validateIntentionData() }

    private fun validateIntentionData() {
        if (cryptoAmount < 1) { throw InvalidAttributeValueException("The value of cryptoAmount is not valid") }
        if (cryptoPrice <= 0) { throw InvalidAttributeValueException("The value of cryptoPrice is not valid") }
    }
}
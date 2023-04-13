package ar.edu.unq.desapp.grupog.backenddesappapi.model

import ar.edu.unq.desapp.grupog.backenddesappapi.model.trxHelpers.TrxType
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class Intention(
    @Column(nullable = false) var cryptoActive: CryptoActiveName,
    @Column(nullable = false) var cryptoAmount: Int,
    @Column(nullable = false) var cryptoPrice: Double,
    @Column(nullable = false) var arsAmount: Double,
    @ManyToOne var user: User,
    @Column(nullable = false) var trxType: TrxType,
    @Column(nullable = false) var date: LocalDateTime
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(nullable = false)
    var available: Boolean = true
}
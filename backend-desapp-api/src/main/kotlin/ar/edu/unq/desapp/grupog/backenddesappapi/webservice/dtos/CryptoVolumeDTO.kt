package ar.edu.unq.desapp.grupog.backenddesappapi.webservice.dtos

import java.time.LocalDateTime

class CryptoVolumeDTO (
    var date: LocalDateTime?,
    var usdAmount: Double,
        var arsAmount: Double,
        var actives: List<CryptoActiveDTO>
)


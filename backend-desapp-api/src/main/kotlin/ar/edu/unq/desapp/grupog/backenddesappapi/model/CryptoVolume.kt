package ar.edu.unq.desapp.grupog.backenddesappapi.model

import ar.edu.unq.desapp.grupog.backenddesappapi.webservice.dtos.CryptoActiveDTO
import java.time.LocalDateTime

class CryptoVolume (
        var date: LocalDateTime?,
        var usdAmount: Double,
        var arsAmount: Double,
        var actives: List<CryptoActive>
)
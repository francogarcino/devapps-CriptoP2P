package ar.edu.unq.desapp.grupog.backenddesappapi.webservice.dtos

import ar.edu.unq.desapp.grupog.backenddesappapi.model.CryptoActiveName

class CryptoActiveDTO(
        var name: CryptoActiveName,
        var cryptoAmount: Int,
        var cryptoPrice: Double,
        var arsPrice: Double
)
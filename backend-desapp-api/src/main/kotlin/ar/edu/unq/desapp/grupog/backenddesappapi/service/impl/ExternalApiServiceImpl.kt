package ar.edu.unq.desapp.grupog.backenddesappapi.service.impl

import ar.edu.unq.desapp.grupog.backenddesappapi.model.CryptoActiveName
import ar.edu.unq.desapp.grupog.backenddesappapi.model.exceptions.NotFoundValueException
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class ExternalApisService {
    val restTemplate = RestTemplate()

    fun getDollarPrice(): Double {
        val url = "https://www.dolarsi.com/api/api.php?type=valoresprincipales"
        val response = restTemplate.getForEntity(url, Array<Root>::class.java)

        val officialRate = response.body?.first {
            it.casa?.nombre == "Dolar Blue"
        } ?: throw NotFoundValueException()

        return officialRate.casa?.venta?.replace(",", ".")?.toDouble()
            ?: throw NotFoundValueException()
    }

    fun getCryptoPrice(cryptoActiveName: String): Double {
        return if (System.getenv("API_HANDLER").isNullOrBlank()) {
            val url = "https://api.binance.com/api/v3/ticker/price?symbol=$cryptoActiveName"
            val response = restTemplate.getForEntity(url, Symbol::class.java)

            response.body?.price ?: throw NotFoundValueException()
        } else {
            1.0
        }
    }

    fun getCryptosPrices(cryptos: List<CryptoActiveName>): Map<String, Double> {
        return if (System.getenv("API_HANDLER").isNullOrBlank()) {
            cryptos.map { c -> c.name }.toSet()
            val symbols =
                cryptos.joinToString(
                    prefix = "[",
                    postfix = "]",
                    separator = ",",
                    transform = { c -> "\"" + c + "\"" })
            val url = "https://api.binance.com/api/v3/ticker/price?symbols=$symbols"

            val response = restTemplate.getForEntity(url, Array<Symbol>::class.java)

            response.body?.associate { it.symbol!! to it.price!! }
                ?: throw NotFoundValueException()
        } else {
            val map = mutableMapOf<String, Double>()
            for (c in cryptos) {
                map[c.name] = 1.0
            }
            map
        }
    }
}

data class Casa(
    var compra: String?,
    var venta: String?,
    var nombre: String?,
    var fecha: String?,
)

data class Root(
    var casa: Casa?,
)

data class Symbol(
    var symbol: String?,
    var price: Double?,
)
package ar.edu.unq.desapp.grupog.backenddesappapi.service.impl

import ar.edu.unq.desapp.grupog.backenddesappapi.model.CryptoActiveName
import ar.edu.unq.desapp.grupog.backenddesappapi.model.CryptoActiveName.*
import ar.edu.unq.desapp.grupog.backenddesappapi.model.exceptions.NotFoundValueException
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Cacheable
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.lang.RuntimeException
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@Service
class ExternalApisServiceImpl {
    val restTemplate = RestTemplate()
    val logger = LoggerFactory.getLogger(ExternalApisServiceImpl::class.java)

    fun getDollarPrice(): Double {
        val url = "https://www.dolarsi.com/api/api.php?type=valoresprincipales"
        val response = restTemplate.getForEntity(url, Array<Root>::class.java)

        val officialRate = response.body?.first {
            it.casa?.nombre == "Dolar Blue"
        } ?: throw NotFoundValueException()

        return officialRate.casa?.venta?.replace(",", ".")?.toDouble()
            ?: throw NotFoundValueException()
    }

    fun getCryptoPrice(cryptoActiveName: CryptoActiveName): Double {
        return if (System.getenv("API_HANDLER").isNullOrBlank()) {
            val url = "https://api.binance.com/api/v3/ticker/price?symbol=${cryptoActiveName.name}"
            val response = restTemplate.getForEntity(url, Symbol::class.java)

            response.body?.price ?: throw NotFoundValueException()
        } else {
            1.0
        }
    }

    @Cacheable(
        value = ["daily"],
        key = "#cryptoActiveName"
    )
    fun getLast24Hours(cryptoActiveName: CryptoActiveName): List<PriceWithTime> {
        return if (System.getenv("API_HANDLER").isNullOrBlank()) {
            val url = "https://api.binance.com/api/v3/klines?" +
                    "symbol=${cryptoActiveName.name}&interval=3m&limit=480"
            val response = restTemplate.getForEntity(url, Array<Array<Any>>::class.java)

            if (response.body != null) {
                val filtered = response.body!!.map {
                    item -> PriceWithTime(
                        LocalDateTime.ofInstant(
                            Instant.ofEpochMilli(item[0] as Long),
                            ZoneId.systemDefault()
                        ),
                        item[1] as String
                    )
                }

                filtered
            } else {
                throw RuntimeException("Something fail")
            }
        } else {
            throw RuntimeException("Something fail")
        }
    }

    @Scheduled(fixedDelay = 600000)
    fun priceEveryTenMinutes() {
        var actives = setOf(ALICEUSDT, MATICUSDT, AXSUSDT, AAVEUSDT, ATOMUSDT, NEOUSDT, DOTUSDT,
            ETHUSDT, CAKEUSDT, BTCUSDT, BNBUSDT, ADAUSDT, TRXUSDT, AUDIOUSDT)
        val prices = mutableMapOf<CryptoActiveName,PriceWithTime>()
        actives.forEach { active ->
            prices[active] = PriceWithTime(
                LocalDateTime.now(), getCryptoPrice(active).toString()
            )
        }
        logger.info("""Prices updated: 
            |$prices""".trimMargin())
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

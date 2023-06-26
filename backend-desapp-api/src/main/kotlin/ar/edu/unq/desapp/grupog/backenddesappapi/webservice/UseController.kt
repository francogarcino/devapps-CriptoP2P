package ar.edu.unq.desapp.grupog.backenddesappapi.webservice

import ar.edu.unq.desapp.grupog.backenddesappapi.model.CryptoActiveName
import ar.edu.unq.desapp.grupog.backenddesappapi.service.impl.ExternalApisService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

// TODO borrar de la branch

@RestController
@CrossOrigin
@RequestMapping("/values")
class UseController {
    @Autowired lateinit var s : ExternalApisService

    @GetMapping("/dolar")
    fun dolar() = s.getDollarPrice()

    @GetMapping("/crypto/{c}")
    fun crypto(@RequestParam c: String) = s.getCryptoPrice(c)

    @GetMapping("/cs/{c}")
    fun cryptos(@RequestBody c: List<CryptoActiveName>) = s.getCryptosPrices(c)
}
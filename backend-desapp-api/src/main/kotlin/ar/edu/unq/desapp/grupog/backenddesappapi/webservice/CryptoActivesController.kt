package ar.edu.unq.desapp.grupog.backenddesappapi.webservice

import ar.edu.unq.desapp.grupog.backenddesappapi.model.CryptoActiveName
import ar.edu.unq.desapp.grupog.backenddesappapi.service.impl.ExternalApisService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

// TODO borrar de la branch

@RestController
@CrossOrigin
@RequestMapping("/values")
class CryptoActivesController {
    @Autowired lateinit var apisService : ExternalApisService

    @GetMapping("/crypto/{c}")
    fun crypto(@PathVariable c: CryptoActiveName) = apisService.getCryptoPrice(c)

    @GetMapping("/daily/{c}")
    fun daily(@PathVariable c: CryptoActiveName) = apisService.getLast24Hours(c)
}
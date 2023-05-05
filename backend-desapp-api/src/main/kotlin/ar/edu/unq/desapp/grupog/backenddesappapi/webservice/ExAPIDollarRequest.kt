package ar.edu.unq.desapp.grupog.backenddesappapi.webservice

import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate

@RestController
@RequestMapping("/dollar")
class ExAPIDollarRequest {
    private val restTemplate = RestTemplate()
    private val token = "BEARER eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE3MTQ3ODk0NTAsInR5cGUiOiJleHRlcm5hbCIsInVzZXIiOiJmcmFuY29nYXJjaW5vQGdtYWlsLmNvbSJ9.G_s9bMipHMw-_KdXNMU43_yGrPZMHT4f8BBNcDyvKotgQGZvQvsRh37bsbttjXB9yKzL_oZmGJbKRC9Yu1Vu7A"

    @GetMapping("/")
    fun getDollarValue(): String? {
        val uri = "https://api.estadisticasbcra.com/usd_of"
        val header = org.springframework.http.HttpHeaders()
        header.set("Authorization", token)
        val request = HttpEntity<String>(header)
        return restTemplate.exchange(uri, HttpMethod.GET, request, String::class.java).body
    }
}
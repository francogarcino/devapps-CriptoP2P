package ar.edu.unq.desapp.grupog.backenddesappapi.webservice

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin
@RequestMapping("/")
class RestController {

    @GetMapping("/health")
    fun healthEndpoint() : ResponseEntity<Any> {
        return ResponseEntity.status(200).body("Working fine!")
    }

}
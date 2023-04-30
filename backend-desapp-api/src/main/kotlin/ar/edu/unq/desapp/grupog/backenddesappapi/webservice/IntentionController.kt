package ar.edu.unq.desapp.grupog.backenddesappapi.webservice

import ar.edu.unq.desapp.grupog.backenddesappapi.model.Intention
import ar.edu.unq.desapp.grupog.backenddesappapi.service.IntentionService
import ar.edu.unq.desapp.grupog.backenddesappapi.webservice.dtos.IntentionDTO
import ar.edu.unq.desapp.grupog.backenddesappapi.webservice.mappers.IntentionMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin
@RequestMapping("/intention")
class IntentionController {
    @Autowired private lateinit var intentionService: IntentionService
    private var mapper = IntentionMapper()

    @GetMapping("/")
    fun getAllIntentions() : ResponseEntity<List<IntentionDTO>> {
        val allIntentions = intentionService.readAll()
        return ResponseEntity.ok(allIntentions.map {
            intention -> mapper.fromIntentionToDTO(intention)
        })
    }

    @GetMapping("/{id}")
    fun getIntention(@PathVariable id: Long) : ResponseEntity<Any> {
        return try {
            ResponseEntity.ok().body(mapper.fromIntentionToDTO(intentionService.read(id)))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(e.message)
        }
    }

    @PostMapping("/new")
    fun createIntention(@RequestBody newIntention : Intention) : ResponseEntity<Any> {
        return try {
            val dto = mapper.fromIntentionToDTO(intentionService.create(newIntention))
            ResponseEntity.ok().body(dto)
        } catch (e : Exception) {
            ResponseEntity.badRequest().body(e.message)
        }
    }
}
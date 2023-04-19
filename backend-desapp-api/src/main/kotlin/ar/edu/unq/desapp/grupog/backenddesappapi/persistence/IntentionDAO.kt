package ar.edu.unq.desapp.grupog.backenddesappapi.persistence

import ar.edu.unq.desapp.grupog.backenddesappapi.model.Intention
import org.springframework.data.repository.CrudRepository

interface IntentionDAO : CrudRepository<Intention, Long> {
}
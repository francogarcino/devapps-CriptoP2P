package ar.edu.unq.desapp.grupog.backenddesappapi.persistence

import ar.edu.unq.desapp.grupog.backenddesappapi.model.Intention
import org.springframework.data.jpa.repository.JpaRepository

interface IntentionDAO : JpaRepository<Intention, Long> {
}
package ar.edu.unq.desapp.grupog.backenddesappapi.persistence

import ar.edu.unq.desapp.grupog.backenddesappapi.model.Intention
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface IntentionDAO : JpaRepository<Intention, Long> {
    @Query("FROM Intention i WHERE i.available")
    fun getActiveIntentions() : List<Intention>
}
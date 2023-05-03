package ar.edu.unq.desapp.grupog.backenddesappapi.service

import ar.edu.unq.desapp.grupog.backenddesappapi.model.Intention

interface IntentionService : CrudService<Intention> {
    fun getActiveIntentions() : List<Intention>
}
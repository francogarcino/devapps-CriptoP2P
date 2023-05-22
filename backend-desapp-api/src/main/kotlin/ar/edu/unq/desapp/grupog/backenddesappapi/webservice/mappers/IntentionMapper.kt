package ar.edu.unq.desapp.grupog.backenddesappapi.webservice.mappers

import ar.edu.unq.desapp.grupog.backenddesappapi.model.Intention
import ar.edu.unq.desapp.grupog.backenddesappapi.webservice.dtos.IntentionDTO

class IntentionMapper {
    private val userMapper = UserMapper()
    fun fromIntentionToDTO(intention: Intention) : IntentionDTO {
        val userDTO = userMapper.fromUserToDTO(intention.getUserFromIntention())
        return IntentionDTO(intention.getId(),
                intention.getCryptoActive(),
                intention.getCryptoAmount(),
                intention.getCryptoPrice(),
                intention.getArsAmount(),
                userDTO,
                intention.getTrxType(),
                intention.getDate(),
                intention.available )
    }
}
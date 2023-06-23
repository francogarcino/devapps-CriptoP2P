package ar.edu.unq.desapp.grupog.backenddesappapi.webservice.mappers

import ar.edu.unq.desapp.grupog.backenddesappapi.model.Intention
import ar.edu.unq.desapp.grupog.backenddesappapi.model.User
import ar.edu.unq.desapp.grupog.backenddesappapi.webservice.dtos.IntentionCreateDTO
import ar.edu.unq.desapp.grupog.backenddesappapi.webservice.dtos.IntentionDTO
import java.time.LocalDateTime

class IntentionMapper {
    private val userMapper = UserMapper()
    fun fromIntentionToDTO(intention: Intention) : IntentionDTO {
        val userDTO = userMapper.fromUserToDTO(intention.getUserFromIntention())
        return IntentionDTO(intention.getId()!!,
                intention.getCryptoActive(),
                intention.getCryptoAmount(),
                intention.getCryptoPrice(),
                intention.getArsAmount(),
                userDTO,
                intention.getTrxType(),
                intention.getDate(),
                intention.available)
    }
    fun fromCreateDTOToIntention(dto: IntentionCreateDTO, user: User) : Intention {
        return Intention(dto.cryptoActive,
            dto.cryptoAmount,
            dto.cryptoPrice,
            user,
            dto.trxType,
            LocalDateTime.now())
    }
}
package ar.edu.unq.desapp.grupog.backenddesappapi.webservice.mappers

import ar.edu.unq.desapp.grupog.backenddesappapi.model.User
import ar.edu.unq.desapp.grupog.backenddesappapi.webservice.dtos.UserCreateDTO
import ar.edu.unq.desapp.grupog.backenddesappapi.webservice.dtos.UserDTO
import ar.edu.unq.desapp.grupog.backenddesappapi.webservice.dtos.UserStatsDTO

class UserMapper {
    fun fromUserToDTO(user: User) : UserDTO {
        return UserDTO(user.id, user.firstName, user.lastName, user.email, user.cvu, user.wallet)
    }

    fun fromDataToStatsDTO(pair: Pair<User, Int>) : UserStatsDTO {
        return UserStatsDTO(
            pair.first.firstName,
            pair.first.lastName,
            pair.second,
            pair.first.reputation
        )
    }

    fun fromCreateDTOToUser(dto: UserCreateDTO) : User {
        return User(dto.firstName, dto.lastName, dto.email, dto.address, dto.password, dto.cvu, dto.wallet)
    }
}
package ar.edu.unq.desapp.grupog.backenddesappapi.service

import ar.edu.unq.desapp.grupog.backenddesappapi.model.CryptoVolume
import ar.edu.unq.desapp.grupog.backenddesappapi.model.User
import java.time.LocalDateTime


interface UserService : CrudService<User>{
    fun getCryptoVolume(user: User,  initialDate: LocalDateTime, finalDate: LocalDateTime) : CryptoVolume
}
package ar.edu.unq.desapp.grupog.backenddesappapi.service

import ar.edu.unq.desapp.grupog.backenddesappapi.model.User

interface UserService {
    fun create(user: User): User
    fun update(user: User): User
    fun read(userId: Long): User
    fun readAll(): List<User>
    fun delete(userId: Long)
}
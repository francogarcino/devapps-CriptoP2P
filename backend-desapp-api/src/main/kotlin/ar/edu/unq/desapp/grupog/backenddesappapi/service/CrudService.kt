package ar.edu.unq.desapp.grupog.backenddesappapi.service

interface CrudService<T> {
    fun create(entity: T): T
    fun update(entity: T): T
    fun read(entityId: Long): T
    fun readAll(): List<T>
    fun delete(entityId: Long)
    fun deleteAll()
}
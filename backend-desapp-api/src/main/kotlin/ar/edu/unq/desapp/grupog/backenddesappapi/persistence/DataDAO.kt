package ar.edu.unq.desapp.grupog.backenddesappapi.persistence

import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Component

@Component
open class DataDAO {

    @PersistenceContext
    lateinit var entityManager: EntityManager

    fun clear() {
        val nombreDeTablas = entityManager.createNativeQuery("show tables").resultList
        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS=0;").executeUpdate()
        nombreDeTablas.forEach { result ->
            var tabla = ""
            when(result){
                is String -> tabla = result
                is Array<*> -> tabla= result[0].toString()
            }
            entityManager.createNativeQuery("truncate table $tabla").executeUpdate()
        }
        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS=1;").executeUpdate()
    }
}
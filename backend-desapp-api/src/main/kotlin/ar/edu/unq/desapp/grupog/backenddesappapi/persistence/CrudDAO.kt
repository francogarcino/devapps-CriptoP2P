package ar.edu.unq.desapp.grupog.backenddesappapi.persistence

import org.springframework.data.repository.CrudRepository

interface CrudDAO<T> : CrudRepository<T, Long> {
}
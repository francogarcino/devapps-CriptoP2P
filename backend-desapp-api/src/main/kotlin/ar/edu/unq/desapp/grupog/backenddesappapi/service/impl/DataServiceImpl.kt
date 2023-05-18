package ar.edu.unq.desapp.grupog.backenddesappapi.service.impl

import ar.edu.unq.desapp.grupog.backenddesappapi.persistence.DataDAO
import ar.edu.unq.desapp.grupog.backenddesappapi.service.DataService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
open class DataServiceImpl : DataService {

    @Autowired lateinit var dataDAO: DataDAO

    override fun deleteAll() {
        dataDAO.clear()
    }
}
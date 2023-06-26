package ar.edu.unq.desapp.grupog.backenddesappapi.service.impl

import ar.edu.unq.desapp.grupog.backenddesappapi.model.Intention
import ar.edu.unq.desapp.grupog.backenddesappapi.model.exceptions.OutOfRangePriceException
import ar.edu.unq.desapp.grupog.backenddesappapi.persistence.IntentionDAO
import ar.edu.unq.desapp.grupog.backenddesappapi.service.IntentionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class IntentionServiceImpl : IntentionService {

    @Autowired private lateinit var intentionDAO: IntentionDAO

    @Autowired private lateinit var apisService: ExternalApisServiceImpl

    override fun getActiveIntentions(): List<Intention> {
        return intentionDAO.getActiveIntentions()
    }

    override fun create(entity: Intention): Intention {
        val actualPrice = apisService.getCryptoPrice(entity.getCryptoActive())
        if (!(actualPrice*0.95 <= entity.getCryptoPrice() && entity.getCryptoPrice() <= actualPrice*1.05)) {
            throw OutOfRangePriceException()
        }

        return intentionDAO.save(entity)
    }

    override fun update(entity: Intention): Intention {
        if (entity.getId() != null && intentionDAO.existsById(entity.getId()!!)) return intentionDAO.save(entity)
         else throw NoSuchElementException("The intention to update does not exists")
    }

    override fun read(entityId: Long): Intention {
        return intentionDAO.findById(entityId).orElseThrow { NoSuchElementException("There is no intention with that id") }
    }

    override fun readAll() = intentionDAO.findAll().toList()

    override fun delete(entityId: Long) {
        if (intentionDAO.existsById(entityId)) intentionDAO.deleteById(entityId)
         else throw NoSuchElementException("There is no intention with that id")
    }

    override fun deleteAll() { intentionDAO.deleteAll() }
}
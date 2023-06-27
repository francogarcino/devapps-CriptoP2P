package ar.edu.unq.desapp.grupog.backenddesappapi.webservice.cache

import ar.edu.unq.desapp.grupog.backenddesappapi.model.CryptoActiveName
import ar.edu.unq.desapp.grupog.backenddesappapi.service.impl.PriceWithTime
import org.ehcache.event.CacheEvent
import org.ehcache.event.CacheEventListener
import org.slf4j.LoggerFactory

class CacheEventLogger : CacheEventListener<CryptoActiveName, List<PriceWithTime>>{
    val log = LoggerFactory.getLogger(CacheEventLogger::class.java)

    override fun onEvent(event: CacheEvent<out CryptoActiveName, out List<PriceWithTime>>?) {
        log.info("CacheLogger Triggered", event!!.key, event.oldValue, event.newValue)
    }
}
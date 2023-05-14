package ar.edu.unq.desapp.grupog.backenddesappapi.service

import ar.edu.unq.desapp.grupog.backenddesappapi.model.User
import jakarta.annotation.PostConstruct
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
@Transactional
class InitServiceInMemory {

    protected val logger: Log = LogFactory.getLog(javaClass)

    @Value("\${spring.datasource.driverClassName}")
    private val className: String? = null

    @Autowired private lateinit var userService: UserService

    @PostConstruct
    fun initialize() {
        if (className == "org.h2.Driver") {
            logger.warn("Init Data Using H2 DB")
            fireInitialData()
        }
    }

    private fun fireInitialData() {
        val user1 = User("Franco", "Garcino", "franco@gmail.com", "any address 123",
            "myPassword1", "0123456789012345678901", "10101010")
        val user2 = User("Valentin", "Ferreyra", "valentin@gmail.com", "any address 234",
            "myPassword2", "1098765432109876543210", "01010101")
        val user3 = User("Lucas", "Ziegemann", "lucas@gmail.com", "any address 345",
            "myPassword3", "1155448822663355774488", "22222222")
        userService.create(user1)
        userService.create(user2)
        userService.create(user3)
    }
}
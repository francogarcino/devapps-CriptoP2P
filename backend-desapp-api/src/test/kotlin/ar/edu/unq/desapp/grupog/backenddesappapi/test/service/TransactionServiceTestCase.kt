package ar.edu.unq.desapp.grupog.backenddesappapi.test.service

import ar.edu.unq.desapp.grupog.backenddesappapi.service.TransactionService
import ar.edu.unq.desapp.grupog.backenddesappapi.test.utils.TransactionBuilder
import ar.edu.unq.desapp.grupog.backenddesappapi.test.utils.UserBuilder
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.stereotype.Service

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TransactionServiceTestCase {
    @Autowired private lateinit var transactionService: TransactionService
    private var transactionBuilder = TransactionBuilder()
    private var userBuilder = UserBuilder()

}
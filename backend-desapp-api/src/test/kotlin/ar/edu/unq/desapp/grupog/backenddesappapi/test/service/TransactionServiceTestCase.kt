package ar.edu.unq.desapp.grupog.backenddesappapi.test.service

import ar.edu.unq.desapp.grupog.backenddesappapi.model.CryptoActiveName
import ar.edu.unq.desapp.grupog.backenddesappapi.model.trxHelpers.TrxType
import ar.edu.unq.desapp.grupog.backenddesappapi.service.DataService
import ar.edu.unq.desapp.grupog.backenddesappapi.service.IntentionService
import ar.edu.unq.desapp.grupog.backenddesappapi.service.TransactionService
import ar.edu.unq.desapp.grupog.backenddesappapi.service.UserService
import ar.edu.unq.desapp.grupog.backenddesappapi.service.impl.ExternalApisServiceImpl
import ar.edu.unq.desapp.grupog.backenddesappapi.test.utils.TransactionBuilder
import ar.edu.unq.desapp.grupog.backenddesappapi.test.utils.UserBuilder
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TransactionServiceTestCase {
    @Autowired private lateinit var transactionService: TransactionService
    @Autowired private lateinit var userService: UserService
    @Autowired private lateinit var intentionService: IntentionService
    @Autowired private lateinit var apisService: ExternalApisServiceImpl
    @Autowired private lateinit var dataService: DataService

    private lateinit var transactionBuilder : TransactionBuilder
    private lateinit var userBuilder : UserBuilder

    @BeforeEach
    fun setUp() {
        transactionBuilder = TransactionBuilder()
        userBuilder = UserBuilder()
        dataService.deleteAll()
    }

    @Test
    fun testCreate_Example() {
        val user = userBuilder.build()
        val anotherUser = userBuilder.withEmail("email@hotmail.com")
            .withCVU("0123456789876543210000")
            .withWallet("20230501")
            .build()
        userService.create(user)
        userService.create(anotherUser)

        val intention = user.createIntention(CryptoActiveName.ETHUSDT, 100, apisService.getCryptoPrice(CryptoActiveName.ETHUSDT), TrxType.BUY)
        intentionService.create(intention)

        val transaction = anotherUser.beginTransaction(intention, apisService.getCryptoPrice(CryptoActiveName.ETHUSDT))

        transactionService.create(transaction)
        val read = transactionService.read(transaction.id!!)

        Assertions.assertNotNull(read.id!!)
    }

    // @AfterEach fun teardown() { dataService.deleteAll() }

}
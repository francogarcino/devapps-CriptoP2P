package ar.edu.unq.desapp.grupog.backenddesappapi.test.model

import ar.edu.unq.desapp.grupog.backenddesappapi.model.CryptoActiveName
import ar.edu.unq.desapp.grupog.backenddesappapi.model.exceptions.trx.ActionOnEndedTransactionException
import ar.edu.unq.desapp.grupog.backenddesappapi.model.trxHelpers.TrxStatus
import ar.edu.unq.desapp.grupog.backenddesappapi.model.trxHelpers.TrxType
import ar.edu.unq.desapp.grupog.backenddesappapi.test.utils.IntentionBuilder
import ar.edu.unq.desapp.grupog.backenddesappapi.test.utils.TransactionBuilder
import ar.edu.unq.desapp.grupog.backenddesappapi.test.utils.UserBuilder
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WorkflowTestCase {

    private lateinit var userBuilder : UserBuilder
    private lateinit var transactionBuilder: TransactionBuilder
    private lateinit var intentionBuilder: IntentionBuilder

    @BeforeEach
    fun setUp() {
        userBuilder = UserBuilder()
        transactionBuilder = TransactionBuilder()
        intentionBuilder = IntentionBuilder()
    }

    @Test
    fun testFlow_AfterCancelTheTransactionStateChangesToCancelled() {
        val defaultUser = userBuilder.build()
        val anotherUser = userBuilder.withEmail("another@gmail.com")
            .withCVU("6600660066006600660066")
            .withWallet("80000000")
            .build()

        val intention = defaultUser.createIntention(CryptoActiveName.ETHUSDT, 20, 1.0, TrxType.BUY)
        val trx = anotherUser.beginTransaction(intention)
        anotherUser.transferMoneyToBankAccount(trx)
        defaultUser.cancelTransaction(trx)

        Assertions.assertEquals(TrxStatus.CANCELLED, trx.status)
    }

    @Test
    fun testFlow_AfterCancelTheUserCannotReleaseCryptos() {
        val defaultUser = userBuilder.build()
        val anotherUser = userBuilder.withEmail("another@gmail.com")
            .withCVU("6600660066006600660066")
            .withWallet("80000000")
            .build()

        val intention = defaultUser.createIntention(CryptoActiveName.ETHUSDT, 20, 1.0, TrxType.BUY)
        val trx = anotherUser.beginTransaction(intention)
        anotherUser.transferMoneyToBankAccount(trx)
        defaultUser.cancelTransaction(trx)

        val expectedMsg = ActionOnEndedTransactionException().message
        try { defaultUser.releaseCrypto(trx) } catch (e: Throwable) {
            Assertions.assertEquals(expectedMsg, e.message)
        }
    }

    @Test
    fun testFlow_AfterReleaseCryptoTheTransactionStateChangeToDone() {
        val defaultUser = userBuilder.build()
        val anotherUser = userBuilder.withEmail("another@gmail.com")
            .withCVU("6600660066006600660066")
            .withWallet("80000000")
            .build()

        val intention = defaultUser.createIntention(CryptoActiveName.ETHUSDT, 20, 1.0, TrxType.BUY)
        val trx = anotherUser.beginTransaction(intention)
        anotherUser.transferMoneyToBankAccount(trx)
        defaultUser.releaseCrypto(trx)

        Assertions.assertEquals(TrxStatus.DONE, trx.status)
    }

}
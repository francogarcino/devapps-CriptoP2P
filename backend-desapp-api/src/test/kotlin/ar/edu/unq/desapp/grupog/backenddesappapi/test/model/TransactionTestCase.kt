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
class TransactionTestCase {

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
    fun testTrxState_WhenCancelledShouldNotExecuteActions() {
        val defaultUser = userBuilder.build()
        val anotherUser = userBuilder.withEmail("another@gmail.com")
            .withCVU("6600660066006600660066")
            .withWallet("80000000")
            .build()

        val intention = defaultUser.createIntention(CryptoActiveName.ETHUSDT, 20, 1.0, TrxType.BUY)
        val trx = anotherUser.beginTransaction(intention)
        defaultUser.cancelTransaction(trx)

        val expectedMsg = ActionOnEndedTransactionException().message
        try { anotherUser.transferMoneyToBankAccount(trx) } catch (e: Throwable) {
            Assertions.assertEquals(expectedMsg, e.message)
        }
        try { defaultUser.releaseCrypto(trx) } catch (e: Throwable) {
            Assertions.assertEquals(expectedMsg, e.message)
        }
        try { defaultUser.cancelTransaction(trx) } catch (e: Throwable) {
            Assertions.assertEquals(expectedMsg, e.message)
        }
    }
}
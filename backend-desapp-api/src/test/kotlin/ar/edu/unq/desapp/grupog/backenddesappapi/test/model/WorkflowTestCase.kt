package ar.edu.unq.desapp.grupog.backenddesappapi.test.model

import ar.edu.unq.desapp.grupog.backenddesappapi.model.CryptoActiveName
import ar.edu.unq.desapp.grupog.backenddesappapi.model.exceptions.ExternalUserActionException
import ar.edu.unq.desapp.grupog.backenddesappapi.model.exceptions.trx.ActionOnEndedTransactionException
import ar.edu.unq.desapp.grupog.backenddesappapi.model.exceptions.trx.UnableActionException
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
import java.time.LocalDateTime

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
    fun testFlow_AfterCreatingATransaction_TheIntentionIsNotAvailable() {
        val defaultUser = userBuilder.build()
        val anotherUser = userBuilder.withEmail("another@gmail.com")
            .withCVU("6600660066006600660066")
            .withWallet("80000000")
            .build()

        val intention = defaultUser.createIntention(CryptoActiveName.ETHUSDT, 20, 1.0, TrxType.BUY)

        Assertions.assertTrue(intention.available)

        anotherUser.beginTransaction(intention, 1.0)

        Assertions.assertFalse(intention.available)
    }

    @Test
    fun testFlow_AfterCancelATransaction_TheIntentionIsAvailable() {
        val defaultUser = userBuilder.build()
        val anotherUser = userBuilder.withEmail("another@gmail.com")
            .withCVU("6600660066006600660066")
            .withWallet("80000000")
            .build()

        val intention = defaultUser.createIntention(CryptoActiveName.ETHUSDT, 20, 1.0, TrxType.BUY)

        Assertions.assertTrue(intention.available)

        val trx = anotherUser.beginTransaction(intention, 1.0)

        Assertions.assertFalse(intention.available)

        anotherUser.cancelTransaction(trx)

        Assertions.assertTrue(intention.available)
    }

    @Test
    fun testFlow_TheUserCannotReleaseCryptosWhenTheTransactionStateIsWaiting() {
        val defaultUser = userBuilder.build()
        val anotherUser = userBuilder.withEmail("another@gmail.com")
            .withCVU("6600660066006600660066")
            .withWallet("80000000")
            .build()

        val intention = defaultUser.createIntention(CryptoActiveName.ETHUSDT, 20, 1.0, TrxType.BUY)
        val trx = anotherUser.beginTransaction(intention, 1.0)

        val expectedMsg = UnableActionException().message
        try { defaultUser.releaseCrypto(trx) } catch (e: Throwable) {
            Assertions.assertEquals(expectedMsg, e.message)
        }
    }

    @Test
    fun testFlow_AfterCancelTheTransactionStateChangesToCancelledWhenTheTransactionStateIsWaiting() {
        val defaultUser = userBuilder.build()
        val anotherUser = userBuilder.withEmail("another@gmail.com")
            .withCVU("6600660066006600660066")
            .withWallet("80000000")
            .build()

        val intention = defaultUser.createIntention(CryptoActiveName.ETHUSDT, 20, 1.0, TrxType.BUY)
        val trx = anotherUser.beginTransaction(intention, 1.0)
        anotherUser.cancelTransaction(trx)

        Assertions.assertEquals(TrxStatus.CANCELLED, trx.status)
    }

    @Test
    fun testFlow_AfterCancelTheUserCannotTransferMoney() {
        val defaultUser = userBuilder.build()
        val anotherUser = userBuilder.withEmail("another@gmail.com")
            .withCVU("6600660066006600660066")
            .withWallet("80000000")
            .build()

        val intention = defaultUser.createIntention(CryptoActiveName.ETHUSDT, 20, 1.0, TrxType.BUY)
        val trx = anotherUser.beginTransaction(intention, 1.0)
        anotherUser.cancelTransaction(trx)

        val expectedMsg = ActionOnEndedTransactionException().message
        try { defaultUser.transferMoneyToBankAccount(trx) } catch (e: Throwable) {
            Assertions.assertEquals(expectedMsg, e.message)
        }
    }

    @Test
    fun testFlow_AfterTransferMoneyTheTransactionStateChangeToChecking() {
        val defaultUser = userBuilder.build()
        val anotherUser = userBuilder.withEmail("another@gmail.com")
            .withCVU("6600660066006600660066")
            .withWallet("80000000")
            .build()

        val intention = defaultUser.createIntention(CryptoActiveName.ETHUSDT, 20, 1.0, TrxType.BUY)
        val trx = anotherUser.beginTransaction(intention, 1.0)
        defaultUser.transferMoneyToBankAccount(trx)

        Assertions.assertEquals(TrxStatus.CHECKING, trx.status)
    }

    @Test
    fun testFlow_TheUserCannotTransferMoneyWhenTheTransactionStateIsChecking() {
        val defaultUser = userBuilder.build()
        val anotherUser = userBuilder.withEmail("another@gmail.com")
            .withCVU("6600660066006600660066")
            .withWallet("80000000")
            .build()

        val intention = defaultUser.createIntention(CryptoActiveName.ETHUSDT, 20, 1.0, TrxType.BUY)
        val trx = anotherUser.beginTransaction(intention, 1.0)
        defaultUser.transferMoneyToBankAccount(trx)

        val expectedMsg = UnableActionException().message
        try { anotherUser.transferMoneyToBankAccount(trx) } catch (e: Throwable) {
            Assertions.assertEquals(expectedMsg, e.message)
        }
    }

    @Test
    fun testFlow_AfterCancelTheTransactionStateChangesToCancelled() {
        val defaultUser = userBuilder.build()
        val anotherUser = userBuilder.withEmail("another@gmail.com")
            .withCVU("6600660066006600660066")
            .withWallet("80000000")
            .build()

        val intention = defaultUser.createIntention(CryptoActiveName.ETHUSDT, 20, 1.0, TrxType.BUY)
        val trx = anotherUser.beginTransaction(intention, 1.0)
        defaultUser.transferMoneyToBankAccount(trx)
        anotherUser.cancelTransaction(trx)

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
        val trx = anotherUser.beginTransaction(intention, 1.0)
        defaultUser.transferMoneyToBankAccount(trx)
        defaultUser.cancelTransaction(trx)

        val expectedMsg = ActionOnEndedTransactionException().message
        try { anotherUser.releaseCrypto(trx) } catch (e: Throwable) {
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
        val trx = anotherUser.beginTransaction(intention, 1.0)
        defaultUser.transferMoneyToBankAccount(trx)
        anotherUser.releaseCrypto(trx)

        Assertions.assertEquals(TrxStatus.DONE, trx.status)
    }

    @Test
    fun testTrxState_WhenCancelledShouldNotExecuteActions() {
        val defaultUser = userBuilder.build()
        val anotherUser = userBuilder.withEmail("another@gmail.com")
            .withCVU("6600660066006600660066")
            .withWallet("80000000")
            .build()

        val intention = defaultUser.createIntention(CryptoActiveName.ETHUSDT, 20, 1.0, TrxType.BUY)
        val trx = anotherUser.beginTransaction(intention, 1.0)
        defaultUser.cancelTransaction(trx)

        val expectedMsg = ActionOnEndedTransactionException().message
        try { defaultUser.transferMoneyToBankAccount(trx) } catch (e: Throwable) {
            Assertions.assertEquals(expectedMsg, e.message)
        }
        try { anotherUser.releaseCrypto(trx) } catch (e: Throwable) {
            Assertions.assertEquals(expectedMsg, e.message)
        }
        try { anotherUser.cancelTransaction(trx) } catch (e: Throwable) {
            Assertions.assertEquals(expectedMsg, e.message)
        }
    }

    @Test
    fun testTrxState_WhenDoneShouldNotExecuteActions() {
        val defaultUser = userBuilder.build()
        val anotherUser = userBuilder.withEmail("another@gmail.com")
            .withCVU("6600660066006600660066")
            .withWallet("80000000")
            .build()

        val intention = defaultUser.createIntention(CryptoActiveName.ETHUSDT, 20, 1.0, TrxType.BUY)
        val trx = anotherUser.beginTransaction(intention, 1.0)
        defaultUser.transferMoneyToBankAccount(trx)
        anotherUser.releaseCrypto(trx)

        val expectedMsg = ActionOnEndedTransactionException().message
        try { defaultUser.transferMoneyToBankAccount(trx) } catch (e: Throwable) {
            Assertions.assertEquals(expectedMsg, e.message)
        }
        try { anotherUser.releaseCrypto(trx) } catch (e: Throwable) {
            Assertions.assertEquals(expectedMsg, e.message)
        }
        try { defaultUser.cancelTransaction(trx) } catch (e: Throwable) {
            Assertions.assertEquals(expectedMsg, e.message)
        }
    }

    @Test
    fun testFlow_WhenAnExternalUserInteractsWithATransaction_ShouldReturnAnException() {
        val defaultUser = userBuilder.build()
        val anotherUser = userBuilder.withCVU("2222222222222222222222")
            .withWallet("98798798").withEmail("aRandomEmail@hotmail.com").build()
        val externalUser = userBuilder.withEmail("external@gmail.com").build()

        val expectedMsg = ExternalUserActionException().message

        val intention = defaultUser.createIntention(CryptoActiveName.ETHUSDT, 20, 1.0, TrxType.BUY)
        val trx = anotherUser.beginTransaction(intention, 1.0)

        try { externalUser.transferMoneyToBankAccount(trx) } catch (e: Throwable) { Assertions.assertEquals(expectedMsg, e.message) }
        defaultUser.transferMoneyToBankAccount(trx)
        try { externalUser.releaseCrypto(trx) } catch (e: Throwable) { Assertions.assertEquals(expectedMsg, e.message) }
        try { externalUser.cancelTransaction(trx) } catch (e: Throwable) { Assertions.assertEquals(expectedMsg, e.message) }
    }

    @Test
    fun testFlow_WhenCompleteOnTime_ReputationIncreaseIn10() {
        val defaultUser = userBuilder.build()
        val anotherUser = userBuilder.withCVU("2222222222222222222222")
            .withWallet("98798798").withEmail("aRandomEmail@hotmail.com").build()

        val intention = defaultUser.createIntention(CryptoActiveName.ETHUSDT, 20, 1.0, TrxType.BUY)
        val trx = anotherUser.beginTransaction(intention, 1.0)

        defaultUser.transferMoneyToBankAccount(trx)
        anotherUser.releaseCrypto(trx)

        Assertions.assertEquals(10, defaultUser.reputation)
        Assertions.assertEquals(10, anotherUser.reputation)
    }

    @Test
    fun testFlow_WhenCompleteAfterTime_ReputationIncreaseIn10() {
        val defaultUser = userBuilder.build()
        val anotherUser = userBuilder.withCVU("2222222222222222222222")
            .withWallet("98798798").withEmail("aRandomEmail@hotmail.com").build()

        val intention = defaultUser.createIntention(CryptoActiveName.ETHUSDT, 20, 1.0, TrxType.BUY)
        val trx = anotherUser.beginTransaction(intention, 1.0)
        trx.apply { creationDate = LocalDateTime.now().minusDays(5) }

        defaultUser.transferMoneyToBankAccount(trx)
        anotherUser.releaseCrypto(trx)

        Assertions.assertEquals(5, defaultUser.reputation)
        Assertions.assertEquals(5, anotherUser.reputation)
    }

    @Test
    fun testFlow_WhenCancelReputationDecreaseIn20() {
        val defaultUser = userBuilder.build()
        val anotherUser = userBuilder.withCVU("2222222222222222222222")
            .withWallet("98798798").withEmail("aRandomEmail@hotmail.com").build()

        val intention = defaultUser.createIntention(CryptoActiveName.ETHUSDT, 20, 1.0, TrxType.BUY)
        val trx = anotherUser.beginTransaction(intention, 1.0)

        anotherUser.apply { reputation = 45 }
        anotherUser.cancelTransaction(trx)
        Assertions.assertEquals(25, anotherUser.reputation)
    }

    @Test
    fun testFlow_WhenCancelReputationItDoesntTurnBelow0() {
        val defaultUser = userBuilder.build()
        val anotherUser = userBuilder.withCVU("2222222222222222222222")
            .withWallet("98798798").withEmail("aRandomEmail@hotmail.com").build()

        val intention = defaultUser.createIntention(CryptoActiveName.ETHUSDT, 20, 1.0, TrxType.BUY)
        val trx = anotherUser.beginTransaction(intention, 1.0)

        anotherUser.cancelTransaction(trx)
        Assertions.assertEquals(0, anotherUser.reputation)
        Assertions.assertEquals(TrxStatus.CANCELLED, trx.status)
    }

}
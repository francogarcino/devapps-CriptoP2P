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
    fun testFlow_TheUserCannotReleaseCryptosWhenTheTransactionStateIsWaiting() {
        val defaultUser = userBuilder.build()
        val anotherUser = userBuilder.withEmail("another@gmail.com")
            .withCVU("6600660066006600660066")
            .withWallet("80000000")
            .build()

        val intention = defaultUser.createIntention(CryptoActiveName.ETHUSDT, 20, 1.0, TrxType.BUY)
        val trx = anotherUser.beginTransaction(intention)

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
        val trx = anotherUser.beginTransaction(intention)
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
        val trx = anotherUser.beginTransaction(intention)
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
        val trx = anotherUser.beginTransaction(intention)
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
        val trx = anotherUser.beginTransaction(intention)
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
        val trx = anotherUser.beginTransaction(intention)
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
        val trx = anotherUser.beginTransaction(intention)
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
        val trx = anotherUser.beginTransaction(intention)
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
        val trx = anotherUser.beginTransaction(intention)
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
        val trx = anotherUser.beginTransaction(intention)
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
        val anotherUser = userBuilder.withEmail("another@gmail.com").build()
        val externalUser = userBuilder.withEmail("external@gmail.com").build()

        val expectedMsg = ExternalUserActionException().message

        val intention = defaultUser.createIntention(CryptoActiveName.ETHUSDT, 20, 1.0, TrxType.BUY)
        val trx = anotherUser.beginTransaction(intention)

        try { externalUser.transferMoneyToBankAccount(trx) } catch (e: Throwable) { Assertions.assertEquals(expectedMsg, e.message) }
        defaultUser.transferMoneyToBankAccount(trx)
        try { externalUser.releaseCrypto(trx) } catch (e: Throwable) { Assertions.assertEquals(expectedMsg, e.message) }
        try { externalUser.cancelTransaction(trx) } catch (e: Throwable) { Assertions.assertEquals(expectedMsg, e.message) }
    }

}
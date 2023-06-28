package ar.edu.unq.desapp.grupog.backenddesappapi.test.model

import ar.edu.unq.desapp.grupog.backenddesappapi.model.CryptoActiveName
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
    fun testBuild_CanAccessToIntentionData() {
        val anUser = userBuilder.build()
        val anotherUser = userBuilder.withName("Alternative Name").withCVU("2222222222222222222222")
            .withWallet("98798798").withEmail("aRandomEmail@hotmail.com").build()
        val aTrx = anotherUser.beginTransaction(
            anUser.createIntention(CryptoActiveName.ETHUSDT, 20, 1.0, TrxType.BUY), 1.0
        )
        val anotherTrx = anotherUser.beginTransaction(
            anUser.createIntention(CryptoActiveName.BNBUSDT, 20, 1.0, TrxType.SELL), 1.0
        )

        Assertions.assertEquals(aTrx.typeTransaction(), TrxType.BUY)
        Assertions.assertEquals(anotherTrx.typeTransaction(), TrxType.SELL)

        Assertions.assertEquals(aTrx.cryptoActive(), CryptoActiveName.ETHUSDT)
        Assertions.assertEquals(anotherTrx.cryptoActive(), CryptoActiveName.BNBUSDT)
    }

    @Test
    fun testAddress_TheAddressIsTheExpectedAlwaysOnBuy() {
        val anUser = userBuilder.build()
        val anotherUser = userBuilder.withCVU("2222222222222222222222")
            .withWallet("98798798").withEmail("aRandomEmail@hotmail.com").build()
        val aTrx = anotherUser.beginTransaction(
            anUser.createIntention(CryptoActiveName.ETHUSDT, 20, 1.0, TrxType.BUY), 1.0
        )

        Assertions.assertEquals(aTrx.address(), anotherUser.cvu)
        anUser.transferMoneyToBankAccount(aTrx)

        Assertions.assertEquals(aTrx.address(), anUser.wallet)
        anotherUser.releaseCrypto(aTrx)

        val expected = "Transaction Completed or Finished, no needed address right now"
        Assertions.assertEquals(aTrx.address(), expected)
    }

    @Test
    fun testAddress_TheAddressIsTheExpectedAlwaysOnSell() {
        val anUser = userBuilder.build()
        val anotherUser = userBuilder.withCVU("2222222222222222222222")
            .withWallet("98798798").withEmail("aRandomEmail@hotmail.com").build()
        val aTrx = anotherUser.beginTransaction(
            anUser.createIntention(CryptoActiveName.ETHUSDT, 20, 1.0, TrxType.SELL), 1.0
        )

        Assertions.assertEquals(aTrx.address(), anUser.cvu)
        anotherUser.transferMoneyToBankAccount(aTrx)

        Assertions.assertEquals(aTrx.address(), anotherUser.wallet)
        anUser.releaseCrypto(aTrx)

        val expected = "Transaction Completed or Finished, no needed address right now"
        Assertions.assertEquals(aTrx.address(), expected)
    }

}
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

}
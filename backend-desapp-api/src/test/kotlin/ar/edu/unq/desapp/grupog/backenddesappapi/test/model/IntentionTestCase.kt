package ar.edu.unq.desapp.grupog.backenddesappapi.test.model

import ar.edu.unq.desapp.grupog.backenddesappapi.test.utils.IntentionBuilder
import org.junit.jupiter.api.*
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class IntentionTestCase {

    @Test
    fun testValidation_ShouldThrowAnExceptionForInvalidCryptoAmount() {
        val expectedMsg = "The value of cryptoAmount is not valid"
        try { IntentionBuilder().withCryptoAmount(0).build() } catch (e: Throwable) {
            Assertions.assertEquals(expectedMsg, e.message)
        }
    }

    @Test
    fun testValidation_ShouldThrowAnExceptionForInvalidCryptoPrice() {
        val expectedMsg = "The value of cryptoPrice is not valid"
        try { IntentionBuilder().withCryptoPrice(0.0).build() } catch (e: Throwable) {
            Assertions.assertEquals(expectedMsg, e.message)
        }
        try { IntentionBuilder().withCryptoPrice(-200.8).build() } catch (e: Throwable) {
            Assertions.assertEquals(expectedMsg, e.message)
        }
    }

    @Test
    fun testValidation_ShouldThrowAnExceptionForInvalidArsAmount() {
        val expectedMsg = "The value of arsAmount is not valid"
        try { IntentionBuilder().withArsAmount(0.0).build() } catch (e: Throwable) {
            Assertions.assertEquals(expectedMsg, e.message)
        }
        try { IntentionBuilder().withArsAmount(-2.0).build() } catch (e: Throwable) {
            Assertions.assertEquals(expectedMsg, e.message)
        }
    }
}
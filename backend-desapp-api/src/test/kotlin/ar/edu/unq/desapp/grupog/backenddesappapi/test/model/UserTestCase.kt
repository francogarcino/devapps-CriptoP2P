package ar.edu.unq.desapp.grupog.backenddesappapi.test.model

import ar.edu.unq.desapp.grupog.backenddesappapi.model.exceptions.*
import ar.edu.unq.desapp.grupog.backenddesappapi.test.utils.UserBuilder
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserTestCase {
    private lateinit var builder : UserBuilder
    @BeforeEach fun setup() { builder = UserBuilder() }

    @Test
    fun testBuild_WhenBuildTheIdMustBeNull() {
        Assertions.assertNotNull(builder.build())
    }

    @Test
    fun testValidation_ShouldThrowAnExceptionForInvalidNames() {
        val expectedMsg = InvalidNameAttempException().message
        try { builder.withName("").build() } catch (e: Throwable) {
            Assertions.assertEquals(expectedMsg, e.message!!)
        }
        try { builder.withName("  ").build() } catch (e: Throwable) {
            Assertions.assertEquals(expectedMsg, e.message!!)
        }
        try { builder.withName("     ").build() } catch (e: Throwable) {
            Assertions.assertEquals(expectedMsg, e.message!!)
        }
        try { builder.withName("Li").build() } catch (e: Throwable) {
            Assertions.assertEquals(expectedMsg, e.message!!)
        }
        try { builder.withName("ABCABCABCABCABCABCABCABCABCABCABCABCABCABC").build() } catch (e: Throwable) {
            Assertions.assertEquals(expectedMsg, e.message!!)
        }
    }

    @Test
    fun testValidation_ShouldThrowAnExceptionForInvalidLastnames() {
        val expectedMsg = InvalidNameAttempException().message
        try { builder.withLastname("").build() } catch (e: Throwable) {
            Assertions.assertEquals(expectedMsg, e.message!!)
        }
        try { builder.withLastname("Li").build() } catch (e: Throwable) {
            Assertions.assertEquals(expectedMsg, e.message!!)
        }
        try { builder.withLastname("ABCABCABCABCABCABCABCABCABCABCABCABCABCABC").build() } catch (e: Throwable) {
            Assertions.assertEquals(expectedMsg, e.message!!)
        }
    }

    @Test
    fun testValidation_ShouldThrowAnExceptionForInvalidEmails() {
        val expectedMsg = InvalidEmailException().message
        try { builder.withEmail("").build() } catch (e: Throwable) {
            Assertions.assertEquals(expectedMsg, e.message!!)
        }
        try { builder.withEmail("aBadEmail").build() } catch (e: Throwable) {
            Assertions.assertEquals(expectedMsg, e.message!!)
        }
        try { builder.withEmail("fgr@yahoo.com").build() } catch (e: Throwable) {
            Assertions.assertEquals(expectedMsg, e.message!!)
        }
    }

    @Test
    fun testValidation_ShouldThrowAnExceptionForInvalidPasswords() {
        val expectedMsg = InvalidPasswordException().message
        try { builder.withPassword("").build() } catch (e: Throwable) {
            Assertions.assertEquals(expectedMsg, e.message!!)
        }
        try { builder.withPassword("without-capitals-0").build() } catch (e: Throwable) {
            Assertions.assertEquals(expectedMsg, e.message!!)
        }
        try { builder.withPassword("WITHOUT.SMALLS.0").build() } catch (e: Throwable) {
            Assertions.assertEquals(expectedMsg, e.message!!)
        }
        try { builder.withPassword("WithoutSpecials").build() } catch (e: Throwable) {
            Assertions.assertEquals(expectedMsg, e.message!!)
        }
        try { builder.withPassword("Sh0rt").build() } catch (e: Throwable) {
            Assertions.assertEquals(expectedMsg, e.message!!)
        }
    }

    @Test
    fun testValidation_ShouldThrowAnExceptionForInvalidAddress() {
        val expectedMsg = BadAddressException().message
        try { builder.withAddress("").build() } catch(e: Throwable) {
            Assertions.assertEquals(expectedMsg, e.message!!)
        }
        try { builder.withAddress("Here").build() } catch(e: Throwable) {
            Assertions.assertEquals(expectedMsg, e.message!!)
        }
        try { builder.withAddress("A location in some place of Argentina").build() } catch(e: Throwable) {
            Assertions.assertEquals(expectedMsg, e.message!!)
        }
    }

    @Test
    fun testValidation_ShouldThrowAnExceptionForInvalidCVU() {
        val expectedMsg = BadBankDataException().message
        try { builder.withCVU("").build() } catch (e: Throwable) {
            Assertions.assertEquals(expectedMsg, e.message!!)
        }
        try { builder.withCVU("0123456789").build() } catch (e: Throwable) {
            Assertions.assertEquals(expectedMsg, e.message!!)
        }
        try { builder.withCVU("A random cvu").build() } catch (e: Throwable) {
            Assertions.assertEquals(expectedMsg, e.message!!)
        }
        try { builder.withCVU("010101010101010101010101").build() } catch (e: Throwable) {
            Assertions.assertEquals(expectedMsg, e.message!!)
        }
    }

    @Test
    fun testValidation_ShouldThrowAnExceptionForInvalidWallet() {
        val expectedMsg = BadBankDataException().message
        try { builder.withWallet("").build() } catch (e: Throwable) {
            Assertions.assertEquals(expectedMsg, e.message!!)
        }
        try { builder.withWallet("A wallet").build() } catch (e: Throwable) {
            Assertions.assertEquals(expectedMsg, e.message!!)
        }
        try { builder.withWallet("0123").build() } catch (e: Throwable) {
            Assertions.assertEquals(expectedMsg, e.message!!)
        }
        try { builder.withWallet("0123456789").build() } catch (e: Throwable) {
            Assertions.assertEquals(expectedMsg, e.message!!)
        }
    }

}
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
        try { builder.withName("").build() } catch (e: Throwable) {
            Assertions.assertEquals(InvalidNameAttempException::class, e::class)
        }
        try { builder.withName("Li").build() } catch (e: Throwable) {
            Assertions.assertEquals(InvalidNameAttempException::class, e::class)
        }
        try { builder.withName("ABCABCABCABCABCABCABCABCABCABCABCABCABCABC").build() } catch (e: Throwable) {
            Assertions.assertEquals(InvalidNameAttempException::class, e::class)
        }
    }

    @Test
    fun testValidation_ShouldThrowAnExceptionForInvalidLastnames() {
        try { builder.withLastname("").build() } catch (e: Throwable) {
            Assertions.assertEquals(InvalidNameAttempException::class, e::class)
        }
        try { builder.withLastname("Li").build() } catch (e: Throwable) {
            Assertions.assertEquals(InvalidNameAttempException::class, e::class)
        }
        try { builder.withLastname("ABCABCABCABCABCABCABCABCABCABCABCABCABCABC").build() } catch (e: Throwable) {
            Assertions.assertEquals(InvalidNameAttempException::class, e::class)
        }
    }

    @Test
    fun testValidation_ShouldThrowAnExceptionForInvalidEmails() {
        try { builder.withEmail("").build() } catch (e: Throwable) {
            Assertions.assertEquals(InvalidEmailException::class, e::class)
        }
        try { builder.withEmail("aBadEmail").build() } catch (e: Throwable) {
            Assertions.assertEquals(InvalidEmailException::class, e::class)
        }
        try { builder.withEmail("fgr@yahoo.com").build() } catch (e: Throwable) {
            Assertions.assertEquals(InvalidEmailException::class, e::class)
        }
    }

    @Test
    fun testValidation_ShouldThrowAnExceptionForInvalidPasswords() {
        try { builder.withPassword("").build() } catch (e: Throwable) {
            Assertions.assertEquals(InvalidPasswordException::class, e::class)
        }
        try { builder.withPassword("without-capitals-0").build() } catch (e: Throwable) {
            Assertions.assertEquals(InvalidPasswordException::class, e::class)
        }
        try { builder.withPassword("WITHOUT.SMALLS.0").build() } catch (e: Throwable) {
            Assertions.assertEquals(InvalidPasswordException::class, e::class)
        }
        try { builder.withPassword("WithoutSpecials").build() } catch (e: Throwable) {
            Assertions.assertEquals(InvalidPasswordException::class, e::class)
        }
        try { builder.withPassword("Sh0rt").build() } catch (e: Throwable) {
            Assertions.assertEquals(InvalidPasswordException::class, e::class)
        }
    }

    @Test
    fun testValidation_ShouldThrowAnExceptionForInvalidAddress() {
        try { builder.withAddress("").build() } catch(e: Throwable) {
            Assertions.assertEquals(BadAddressException::class, e::class)
        }
        try { builder.withAddress("Here").build() } catch(e: Throwable) {
            Assertions.assertEquals(BadAddressException::class, e::class)
        }
        try { builder.withAddress("A location in some place of Argentina").build() } catch(e: Throwable) {
            Assertions.assertEquals(BadAddressException::class, e::class)
        }
    }

    @Test
    fun testValidation_ShouldThrowAnExceptionForInvalidCVU() {
        try { builder.withCVU("").build() } catch (e: Throwable) {
            Assertions.assertEquals(BadBankDataException::class, e::class)
        }
        try { builder.withCVU("0123456789").build() } catch (e: Throwable) {
            Assertions.assertEquals(BadBankDataException::class, e::class)
        }
        try { builder.withCVU("A random cvu").build() } catch (e: Throwable) {
            Assertions.assertEquals(BadBankDataException::class, e::class)
        }
        try { builder.withCVU("010101010101010101010101").build() } catch (e: Throwable) {
            Assertions.assertEquals(BadBankDataException::class, e::class)
        }
    }

    @Test
    fun testValidation_ShouldThrowAnExceptionForInvalidWallet() {
        try { builder.withWallet("").build() } catch (e: Throwable) {
            Assertions.assertEquals(BadBankDataException::class, e::class)
        }
        try { builder.withWallet("A wallet").build() } catch (e: Throwable) {
            Assertions.assertEquals(BadBankDataException::class, e::class)
        }
        try { builder.withWallet("0123").build() } catch (e: Throwable) {
            Assertions.assertEquals(BadBankDataException::class, e::class)
        }
        try { builder.withWallet("0123456789").build() } catch (e: Throwable) {
            Assertions.assertEquals(BadBankDataException::class, e::class)
        }
    }

}
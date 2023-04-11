package ar.edu.unq.desapp.grupog.backenddesappapi.test.model
/*
import ar.edu.unq.desapp.grupog.backenddesappapi.model.exceptions.*
import ar.edu.unq.desapp.grupog.backenddesappapi.test.utils.UserBuilder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserTestCase {
    private lateinit var builder : UserBuilder
    @BeforeEach fun setup() { builder = UserBuilder() }

    @Test
    fun testValidation_ShouldThrowAnExceptionForInvalidNames() {
        assertThrows<InvalidNameAttempException> { builder.withName("").build() }
        assertThrows<InvalidNameAttempException> { builder.withName("Li").build() }
        assertThrows<InvalidNameAttempException> { builder.withName("ABCABCABCABCABCABCABCABCABCABCABCABCABCABC").build() }
    }

    @Test
    fun testValidation_ShouldThrowAnExceptionForInvalidLastnames() {
        assertThrows<InvalidNameAttempException> { builder.withLastname("").build() }
        assertThrows<InvalidNameAttempException> { builder.withLastname("Li").build() }
        assertThrows<InvalidNameAttempException> { builder.withLastname("ABCABCABCABCABCABCABCABCABCABCABCABCABCABC").build() }
    }

    @Test
    fun testValidation_ShouldThrowAnExceptionForInvalidEmails() {
        assertThrows<InvalidEmailException> { builder.withEmail("").build() }
        assertThrows<InvalidEmailException> { builder.withEmail("aBadEmail").build() }
        assertThrows<InvalidEmailException> { builder.withEmail("fgr@yahoo.com").build() }
    }

    @Test
    fun testValidation_ShouldThrowAnExceptionForInvalidPasswords() {
        assertThrows<InvalidPasswordException> { builder.withPassword("").build() }
        assertThrows<InvalidPasswordException> { builder.withPassword("without-capitals-0").build() }
        assertThrows<InvalidPasswordException> { builder.withPassword("WITHOUT.SMALLS.0").build() }
        assertThrows<InvalidPasswordException> { builder.withPassword("WithoutSpecials").build() }
        assertThrows<InvalidPasswordException> { builder.withPassword("Sh0rt").build() }
    }

    @Test
    fun testValidation_ShouldThrowAnExceptionForInvalidAddress() {
        assertThrows<BadAddressException> { builder.withAddress("").build() }
        assertThrows<BadAddressException> { builder.withAddress("Here").build() }
        assertThrows<BadAddressException> { builder.withAddress("A location in some place of Argentina").build() }
    }

    @Test
    fun testValidation_ShouldThrowAnExceptionForInvalidCVU() {
        assertThrows<BadBankDataException> { builder.withCVU("").build() }
        assertThrows<BadBankDataException> { builder.withCVU("0123456789").build() }
        assertThrows<BadBankDataException> { builder.withCVU("A random cvu").build() }
        assertThrows<BadBankDataException> { builder.withCVU("010101010101010101010101").build() }
    }

    @Test
    fun testValidation_ShouldThrowAnExceptionForInvalidWallet() {
        assertThrows<BadBankDataException> { builder.withWallet("").build() }
        assertThrows<BadBankDataException> { builder.withWallet("A wallet").build() }
        assertThrows<BadBankDataException> { builder.withWallet("0123").build() }
        assertThrows<BadBankDataException> { builder.withWallet("0123456789").build() }
    }

}*/
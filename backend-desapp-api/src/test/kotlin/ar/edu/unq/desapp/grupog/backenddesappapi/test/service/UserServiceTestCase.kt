package ar.edu.unq.desapp.grupog.backenddesappapi.test.service

import ar.edu.unq.desapp.grupog.backenddesappapi.service.UserService
import ar.edu.unq.desapp.grupog.backenddesappapi.test.utils.UserBuilder
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.lang.RuntimeException

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceTestCase {
    @Autowired private lateinit var userService: UserService
    private lateinit var builder: UserBuilder

    @BeforeEach fun setup() { builder = UserBuilder() }

    @Test
    fun testCreate_CreateAnUserSuccessfully() {
        val createdUser = userService.create(builder.build())
        Assertions.assertNotNull(createdUser.id)
    }

    @Test
    fun testCreate_ShouldThrowAnExceptionWhenEmailIsUsed() {
        userService.create(builder.build())
        assertThrows<RuntimeException> {
            userService.create(
                builder.withName("Franco")
                    .withLastname("Garcino")
                    .build()
            )
        }
    }

    @Test
    fun testCreate_ShouldThrowAnExceptionWhenCVUIsUsed() {
        userService.create(builder.build())
        assertThrows<RuntimeException> {
            userService.create(
                builder.withName("Franco")
                    .withLastname("Garcino")
                    .withEmail("fgarcino@gmail.com")
                    .build()
            )
        }
    }

    @Test
    fun testCreate_ShouldThrowAnExceptionWhenWalletIsUsed() {
        userService.create(builder.build())
        assertThrows<RuntimeException> {
            userService.create(
                builder.withName("Franco")
                    .withLastname("Garcino")
                    .withEmail("fgarcino@gmail.com")
                    .withCVU("2121212121212121212121")
                    .build()
            )
        }
    }

    @Test
    fun testUpdate_ShouldThrowAnExceptionWhenTheUserDoesNotExists() {
        assertThrows<RuntimeException> { userService.update(builder.build()) }
    }

    @Test
    fun testRead_ShowThrowAnExceptionWhenTheIdDoesNotMatchWithAnyUser() {
        assertThrows<RuntimeException> { userService.read(1) }
    }

    @Test
    fun testRead_ShowThrowAnExceptionWhenTheIdIsInvalid() {
        assertThrows<RuntimeException> { userService.read(-1) }
    }

    @AfterEach fun teardown() { userService.deleteAll() }
}
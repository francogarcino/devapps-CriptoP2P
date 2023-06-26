package ar.edu.unq.desapp.grupog.backenddesappapi.test.service

import ar.edu.unq.desapp.grupog.backenddesappapi.model.CryptoActiveName
import ar.edu.unq.desapp.grupog.backenddesappapi.model.exceptions.UserAlreadyRegisteredException
import ar.edu.unq.desapp.grupog.backenddesappapi.model.trxHelpers.TrxType
import ar.edu.unq.desapp.grupog.backenddesappapi.service.IntentionService
import ar.edu.unq.desapp.grupog.backenddesappapi.service.UserService
import ar.edu.unq.desapp.grupog.backenddesappapi.service.impl.ExternalApisServiceImpl
import ar.edu.unq.desapp.grupog.backenddesappapi.test.utils.UserBuilder
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.lang.RuntimeException

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceTestCase {
    @Autowired private lateinit var userService: UserService
    @Autowired private lateinit var intentionService: IntentionService
    @Autowired private lateinit var apisService: ExternalApisServiceImpl
    private lateinit var builder: UserBuilder

    @BeforeEach fun setup() {
        builder = UserBuilder()
        userService.deleteAll()
    }

    @Test
    fun testCreate_CreateAnUserSuccessfully() {
        val createdUser = userService.create(builder.build())
        Assertions.assertNotNull(createdUser.id)
    }

    @Test
    fun testCreate_ShouldThrowAnExceptionWhenEmailIsUsed() {
        val user = userService.create(builder.build())
        val expectedMsg = UserAlreadyRegisteredException("email", user.email).message
        try {
            userService.create(
                builder.withName("Franco")
                    .withLastname("Garcino")
                    .build()
            )
        } catch (e: UserAlreadyRegisteredException) {
            Assertions.assertEquals(expectedMsg, e.message)
        }
    }

    @Test
    fun testCreate_ShouldThrowAnExceptionWhenCVUIsUsed() {
        val user = userService.create(builder.build())
        val expectedMsg = UserAlreadyRegisteredException("cvu", user.cvu).message
        try {
            userService.create(
                builder.withName("Franco")
                    .withLastname("Garcino")
                    .withEmail("fgarcino@gmail.com")
                    .build()
            )
        } catch (e: UserAlreadyRegisteredException) {
            Assertions.assertEquals(expectedMsg, e.message)
        }
    }

    @Test
    fun testCreate_ShouldThrowAnExceptionWhenWalletIsUsed() {
        val user = userService.create(builder.build())
        val expectedMsg = UserAlreadyRegisteredException("wallet", user.wallet).message
        try {
            userService.create(
                builder.withName("Franco")
                    .withLastname("Garcino")
                    .withEmail("fgarcino@gmail.com")
                    .withCVU("2121212121212121212121")
                    .build()
            )
        } catch (e: UserAlreadyRegisteredException) {
            Assertions.assertEquals(expectedMsg, e.message)
        }
    }

    @Test
    fun testUpdate_UpdateSuccessfully() {
        val anUser = userService.create(builder.build())
        anUser.apply { email = "backup-mail@gmail.com" }
        userService.update(anUser)
        val updatedUser = userService.read(anUser.id!!)

        Assertions.assertEquals(anUser.firstName, updatedUser.firstName)
        Assertions.assertEquals(anUser.lastName, updatedUser.lastName)
        Assertions.assertEquals(anUser.address, updatedUser.address)
        Assertions.assertEquals(anUser.cvu, updatedUser.cvu)
        Assertions.assertEquals(anUser.wallet, updatedUser.wallet)
        Assertions.assertEquals(anUser.password, updatedUser.password)
        Assertions.assertEquals(anUser.email, updatedUser.email)
    }

    @Test
    fun testUpdate_ShouldThrowAnExceptionWhenTheUserDoesNotExists() {
        assertThrows<RuntimeException> { userService.update(builder.build()) }
    }
    @Test
    fun testRead_RecoversSimilarObjects() {
        val anUser = userService.create(builder.build())
        val recoveryUser = userService.read(anUser.id!!)

        Assertions.assertEquals(anUser.firstName, recoveryUser.firstName)
        Assertions.assertEquals(anUser.lastName, recoveryUser.lastName)
        Assertions.assertEquals(anUser.address, recoveryUser.address)
        Assertions.assertEquals(anUser.cvu, recoveryUser.cvu)
        Assertions.assertEquals(anUser.wallet, recoveryUser.wallet)
        Assertions.assertEquals(anUser.password, recoveryUser.password)
        Assertions.assertEquals(anUser.email, recoveryUser.email)

        Assertions.assertTrue(anUser !== recoveryUser)
    }

    @Test
    fun testRead_ShouldThrowAnExceptionWhenTheIdDoesNotMatchWithAnyUser() {
        assertThrows<RuntimeException> { userService.read(1) }
    }

    @Test
    fun testRead_ShouldThrowAnExceptionWhenTheIdIsInvalid() {
        assertThrows<RuntimeException> { userService.read(-1) }
    }

    @Test
    fun testReadAll_RecoversAllTheUsersInTheDatabase() {
        val aDefaultUser = userService.create(builder.build())
        val anotherUser = userService.create(
            builder.withEmail("notDefault@hotmail.com")
                .withWallet("12346578")
                .withCVU("0000010000000000100000")
                .build()
        )

        val users = userService.readAll()
        Assertions.assertEquals(2, users.size)
        Assertions.assertTrue(users.any { u -> u.wallet == aDefaultUser.wallet })
        Assertions.assertTrue(users.any { u -> u.wallet == anotherUser.wallet })
    }

    @Test
    fun testReadAll_ShouldReturnsAnEmptyListIfDatabaseHaveNoUsers() {
        Assertions.assertTrue(userService.readAll().isEmpty())
    }

    @Test
    fun testDelete_DeleteTheUserFromDatabase() {
        val anUser = userService.create(builder.build())
        assertDoesNotThrow { userService.read(anUser.id!!) }
        userService.delete(anUser.id!!)
        assertThrows<RuntimeException> { userService.read(anUser.id!!) }
    }

    @Test
    fun testDeleteAll_DeletesAllTheUsersInTheDatabase() {
        userService.create(builder.build())
        Assertions.assertTrue(userService.readAll().isNotEmpty())
        userService.deleteAll()
        Assertions.assertTrue(userService.readAll().isEmpty())
    }

    @Test
    fun testStats_UsersWithoutTrxs() {
        userService.create(builder.build())
        userService.create(builder.withEmail("e@gmail.com").withCVU("0147896321478963214785")
            .withWallet("64646464").build())
        val list = userService.allUserStats()

        Assertions.assertTrue(list.all { p -> p.second == 0 })
    }

    @Test
    fun testStats_UsersWithTrxs() {
        val user = userService.create(builder.build())
        val anotherUser = userService.create(builder.withEmail("e@gmail.com").withCVU("0147896321478963214785")
            .withWallet("64646464").build())
        val intention = intentionService.create(user.createIntention(CryptoActiveName.ETHUSDT, 20, apisService.getCryptoPrice(CryptoActiveName.ETHUSDT), TrxType.BUY))
        val trx = userService.beginTransaction(anotherUser.id!!, intention.getId()!!)

        userService.registerTransfer(user.id!!, trx.id!!)
        userService.registerReleaseCrypto(anotherUser.id!!, trx.id!!)

        val list = userService.allUserStats()

        Assertions.assertTrue(list.all { p -> p.second == 1 })
    }

    @Test
    fun testStats_UsersWithDifferentAmounts() {
        val user = userService.create(builder.build())
        val anotherUser = userService.create(builder.withEmail("e@gmail.com").withCVU("0147896321478963214785")
            .withWallet("64646464").build())
        val externalUser = userService.create(builder.withEmail("ext@gmail.com").withCVU("0147896321478963214000")
            .withWallet("64646000").build())
        val intention = intentionService.create(user.createIntention(CryptoActiveName.ETHUSDT, 20, apisService.getCryptoPrice(CryptoActiveName.ETHUSDT), TrxType.BUY))
        val trx = userService.beginTransaction(anotherUser.id!!, intention.getId()!!)

        userService.registerTransfer(user.id!!, trx.id!!)
        userService.registerReleaseCrypto(anotherUser.id!!, trx.id!!)

        val list = userService.allUserStats()

        Assertions.assertTrue(list.any { p -> p.second == 0 && p.first.email == "ext@gmail.com"})
        Assertions.assertEquals(2, list.filter { p -> p.second == 1 }.size)
    }

//    @AfterEach fun teardown() { userService.deleteAll() }
}
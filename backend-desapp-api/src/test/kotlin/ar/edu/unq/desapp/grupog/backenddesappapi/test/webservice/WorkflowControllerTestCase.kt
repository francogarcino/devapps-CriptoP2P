package ar.edu.unq.desapp.grupog.backenddesappapi.test.webservice

import ar.edu.unq.desapp.grupog.backenddesappapi.service.DataService
import ar.edu.unq.desapp.grupog.backenddesappapi.service.IntentionService
import ar.edu.unq.desapp.grupog.backenddesappapi.service.UserService
import ar.edu.unq.desapp.grupog.backenddesappapi.test.utils.IntentionBuilder
import ar.edu.unq.desapp.grupog.backenddesappapi.test.utils.UserBuilder
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WorkflowControllerTestCase {

    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var context: WebApplicationContext

    @Autowired
    private lateinit var userService: UserService
    @Autowired
    private lateinit var intentionService: IntentionService
    @Autowired
    private lateinit var dataService: DataService

    @BeforeEach
    fun setup() {
        dataService.deleteAll()
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build()
    }

    @Test
    fun testCreateTransactionAndRegisterATransfer() {
        val userCreate = userService.create(UserBuilder().build())
        val userAccept = userService.create(UserBuilder()
            .withEmail("otroemail@gmail.com")
            .withCVU("1212121212454545454578")
            .withWallet("12344321").build())
        val intention = intentionService.create(IntentionBuilder().withUser(userCreate).build())
        val transaction = userService.beginTransaction(userAccept.id!!, intention.getId()!!)
        mockMvc.perform(
            MockMvcRequestBuilders.put("/users/{idUser}/{idTransaction}/registerTransfer",
                userCreate.id, transaction.id)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
    }

    @Test
    fun testCannotRegisterATransferWithAnInvalidUserId() {
        val userCreate = userService.create(UserBuilder().build())
        val userAccept = userService.create(UserBuilder()
            .withEmail("otroemail@gmail.com")
            .withCVU("1212121212454545454578")
            .withWallet("12344321").build())
        val intention = intentionService.create(IntentionBuilder().withUser(userCreate).build())
        val transaction = userService.beginTransaction(userAccept.id!!, intention.getId()!!)
        mockMvc.perform(
            MockMvcRequestBuilders.put("/users/{idUser}/{idTransaction}/registerTransfer",
                "id", transaction.id)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest)
    }

    @Test
    fun testCannotRegisterATransferWithAnUserIdNotPersisted() {
        val userCreate = userService.create(UserBuilder().build())
        val userAccept = userService.create(UserBuilder()
            .withEmail("otroemail@gmail.com")
            .withCVU("1212121212454545454578")
            .withWallet("12344321").build())
        val intention = intentionService.create(IntentionBuilder().withUser(userCreate).build())
        val transaction = userService.beginTransaction(userAccept.id!!, intention.getId()!!)
        mockMvc.perform(
            MockMvcRequestBuilders.put("/users/{idUser}/{idTransaction}/registerTransfer",
                -1, transaction.id)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound)
    }

    @Test
    fun testCreateTransaction_RegisterATransferAndRegisterACryptoRelease() {
        val userCreate = userService.create(UserBuilder().build())
        val userAccept = userService.create(UserBuilder()
            .withEmail("otroemail@gmail.com")
            .withCVU("1212121212454545454578")
            .withWallet("12344321").build())
        val intention = intentionService.create(IntentionBuilder().withUser(userCreate).build())
        val transaction = userService.beginTransaction(userAccept.id!!, intention.getId()!!)
        mockMvc.perform(
            MockMvcRequestBuilders.put("/users/{idUser}/{idTransaction}/registerTransfer",
                userCreate.id, transaction.id)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
        mockMvc.perform(
            MockMvcRequestBuilders.put("/users/{idUser}/{idTransaction}/registerRelease",
                userAccept.id, transaction.id)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
    }

    @Test
    fun testCannotRegisterACryptoReleaseWithAnInvalidUserId() {
        val userCreate = userService.create(UserBuilder().build())
        val userAccept = userService.create(UserBuilder()
            .withEmail("otroemail@gmail.com")
            .withCVU("1212121212454545454578")
            .withWallet("12344321").build())
        val intention = intentionService.create(IntentionBuilder().withUser(userCreate).build())
        val transaction = userService.beginTransaction(userAccept.id!!, intention.getId()!!)
        mockMvc.perform(
            MockMvcRequestBuilders.put("/users/{idUser}/{idTransaction}/registerRelease",
                "id", transaction.id)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest)
    }

    @Test
    fun testCannotRegisterACryptoReleaseWithAnUserIdNotPersisted() {
        val userCreate = userService.create(UserBuilder().build())
        val userAccept = userService.create(UserBuilder()
            .withEmail("otroemail@gmail.com")
            .withCVU("1212121212454545454578")
            .withWallet("12344321").build())
        val intention = intentionService.create(IntentionBuilder().withUser(userCreate).build())
        val transaction = userService.beginTransaction(userAccept.id!!, intention.getId()!!)
        mockMvc.perform(
            MockMvcRequestBuilders.put("/users/{idUser}/{idTransaction}/registerRelease",
                -1, transaction.id)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound)
    }

    @Test
    fun testCreateTransactionAndCancelIt() {
        val userCreate = userService.create(UserBuilder().build())
        val userAccept = userService.create(UserBuilder()
            .withEmail("otroemail@gmail.com")
            .withCVU("1212121212454545454578")
            .withWallet("12344321").build())
        val intention = intentionService.create(IntentionBuilder().withUser(userCreate).build())
        val transaction = userService.beginTransaction(userAccept.id!!, intention.getId()!!)
        mockMvc.perform(
            MockMvcRequestBuilders.put("/users/{idUser}/{idTransaction}/cancelTransaction",
                userCreate.id, transaction.id)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
    }

    @Test
    fun testCannotCancelATransactionWithAnInvalidUserId() {
        val userCreate = userService.create(UserBuilder().build())
        val userAccept = userService.create(UserBuilder()
            .withEmail("otroemail@gmail.com")
            .withCVU("1212121212454545454578")
            .withWallet("12344321").build())
        val intention = intentionService.create(IntentionBuilder().withUser(userCreate).build())
        val transaction = userService.beginTransaction(userAccept.id!!, intention.getId()!!)
        mockMvc.perform(
            MockMvcRequestBuilders.put("/users/{idUser}/{idTransaction}/cancelTransaction",
                "id", transaction.id)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest)
    }

    @Test
    fun testCannotCancelATransactionWithAnUserIdNotPersisted() {
        val userCreate = userService.create(UserBuilder().build())
        val userAccept = userService.create(UserBuilder()
            .withEmail("otroemail@gmail.com")
            .withCVU("1212121212454545454578")
            .withWallet("12344321").build())
        val intention = intentionService.create(IntentionBuilder().withUser(userCreate).build())
        val transaction = userService.beginTransaction(userAccept.id!!, intention.getId()!!)
        mockMvc.perform(
            MockMvcRequestBuilders.put("/users/{idUser}/{idTransaction}/cancelTransaction",
                -1, transaction.id)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound)
    }
}
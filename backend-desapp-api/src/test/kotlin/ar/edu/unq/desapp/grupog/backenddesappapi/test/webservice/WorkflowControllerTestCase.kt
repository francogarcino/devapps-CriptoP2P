package ar.edu.unq.desapp.grupog.backenddesappapi.test.webservice

import ar.edu.unq.desapp.grupog.backenddesappapi.service.DataService
import ar.edu.unq.desapp.grupog.backenddesappapi.service.IntentionService
import ar.edu.unq.desapp.grupog.backenddesappapi.service.UserService
import ar.edu.unq.desapp.grupog.backenddesappapi.test.utils.IntentionBuilder
import ar.edu.unq.desapp.grupog.backenddesappapi.test.utils.LoginDTOBuilder
import ar.edu.unq.desapp.grupog.backenddesappapi.test.utils.UserBuilder
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.Cookie
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

    private val mapper = ObjectMapper()

    @BeforeEach
    fun setup() {
        dataService.deleteAll()
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build()
    }

    @Test
    fun testCreateTransactionAndRegisterATransfer() {
        val cookie = addCookie()
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
                .cookie(cookie)
        ).andExpect(status().isOk)
    }

    @Test
    fun testCannotRegisterATransferWithAnInvalidUserId() {
        val cookie = addCookie()
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
                .cookie(cookie)
        ).andExpect(status().isBadRequest)
    }

    @Test
    fun testCannotRegisterATransferWithAnUserIdNotPersisted() {
        val cookie = addCookie()
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
                .cookie(cookie)
        ).andExpect(status().isNotFound)
    }

    @Test
    fun testCreateTransaction_RegisterATransferAndRegisterACryptoRelease() {
        val cookie = addCookie()
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
                .cookie(cookie)
        ).andExpect(status().isOk)
        mockMvc.perform(
            MockMvcRequestBuilders.put("/users/{idUser}/{idTransaction}/registerRelease",
                userAccept.id, transaction.id)
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(cookie)
        ).andExpect(status().isOk)
    }

    @Test
    fun testCannotRegisterACryptoReleaseWithAnInvalidUserId() {
        val cookie = addCookie()
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
                .cookie(cookie)
        ).andExpect(status().isBadRequest)
    }

    @Test
    fun testCannotRegisterACryptoReleaseWithAnUserIdNotPersisted() {
        val cookie = addCookie()
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
                .cookie(cookie)
        ).andExpect(status().isNotFound)
    }

    @Test
    fun testCreateTransactionAndCancelIt() {
        val cookie = addCookie()
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
                .cookie(cookie)
        ).andExpect(status().isOk)
    }

    @Test
    fun testCannotCancelATransactionWithAnInvalidUserId() {
        val cookie = addCookie()
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
                .cookie(cookie)
        ).andExpect(status().isBadRequest)
    }

    @Test
    fun testCannotCancelATransactionWithAnUserIdNotPersisted() {
        val cookie = addCookie()
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
                .cookie(cookie)
        ).andExpect(status().isNotFound)
    }

    private fun addCookie(): Cookie? {
        userService.create(UserBuilder().withEmail("defaultemail2@gmail.com")
            .withCVU("0011223344556677889911").withWallet("10254721").build())
        val login = LoginDTOBuilder().build()
        val response = mockMvc.perform(
            MockMvcRequestBuilders.post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(login))
        ).andExpect(status().isOk)
        return response.andReturn().response.cookies[0]
    }
}
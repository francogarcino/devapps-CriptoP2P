package ar.edu.unq.desapp.grupog.backenddesappapi.test.webservice

import ar.edu.unq.desapp.grupog.backenddesappapi.service.DataService
import ar.edu.unq.desapp.grupog.backenddesappapi.service.IntentionService
import ar.edu.unq.desapp.grupog.backenddesappapi.service.UserService
import ar.edu.unq.desapp.grupog.backenddesappapi.test.utils.IntentionBuilder
import ar.edu.unq.desapp.grupog.backenddesappapi.test.utils.LoginDTOBuilder
import ar.edu.unq.desapp.grupog.backenddesappapi.test.utils.UserBuilder
import com.fasterxml.jackson.databind.ObjectMapper
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
        val header = addHeader()
        val userCreate = userService.findByEmail("defaultemail2@gmail.com")
        val userAccept = userService.create(UserBuilder().build())
        val intention = intentionService.create(IntentionBuilder().withUser(userCreate).build())
        val transaction = userService.beginTransaction(userAccept.id!!, intention.getId()!!)
        mockMvc.perform(
            MockMvcRequestBuilders.put("/users/registerTransfer/{idTransaction}",
                transaction.id)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", header)
        ).andExpect(status().isOk)
    }

    @Test
    fun testCannotRegisterATransferWithAnInvalidTransactionId() {
        mockMvc.perform(
            MockMvcRequestBuilders.put("/users/registerTransfer/{idTransaction}",
                "id")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", addHeader())
        ).andExpect(status().isBadRequest)
    }

    @Test
    fun testCannotRegisterATransferWithAnTransactionIdNotPersisted() {
        mockMvc.perform(
            MockMvcRequestBuilders.put("/users/registerTransfer/{idTransaction}",
                -1)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", addHeader())
        ).andExpect(status().isNotFound)
    }

    @Test
    fun testCreateTransaction_RegisterATransferAndRegisterACryptoRelease() {
        val headerCreate = addHeader()
        val userCreate = userService.findByEmail("defaultemail2@gmail.com")
        val userAccept = userService.create(
            UserBuilder().withEmail("otroemail@gmail.com").withCVU("1212121212454545454578")
                .withWallet("12344321").build())
        val intention = intentionService.create(IntentionBuilder().withUser(userCreate).build())
        val transaction = userService.beginTransaction(userAccept.id!!, intention.getId()!!)
        mockMvc.perform(
            MockMvcRequestBuilders.put("/users/registerTransfer/{idTransaction}",
                transaction.id)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", headerCreate)
        ).andExpect(status().isOk)

        val login = LoginDTOBuilder().withEmail("otroemail@gmail.com").build()
        val token = mockMvc.perform(
            MockMvcRequestBuilders.post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(login))
        ).andExpect(status().isOk).andReturn().response.contentAsString
        val headerAccept = "Bearer ${token.substring(10, token.length - 2)}"

        mockMvc.perform(
            MockMvcRequestBuilders.put("/users/registerRelease/{idTransaction}",
                transaction.id)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", headerAccept)
        ).andExpect(status().isOk)
    }

    @Test
    fun testCannotRegisterACryptoReleaseWithAnInvalidTransactionId() {
        mockMvc.perform(
            MockMvcRequestBuilders.put("/users/registerRelease/{idTransaction}",
                "id")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", addHeader())
        ).andExpect(status().isBadRequest)
    }

    @Test
    fun testCannotRegisterACryptoReleaseWithAnTransactionIdNotPersisted() {
        mockMvc.perform(
            MockMvcRequestBuilders.put("/users/registerRelease/{idTransaction}",
                -1)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", addHeader())
        ).andExpect(status().isNotFound)
    }

    @Test
    fun testCreateTransactionAndCancelIt() {
        val header = addHeader()
        val userCreate = userService.create(UserBuilder().build())
        val userAccept = userService.findByEmail("defaultemail2@gmail.com")
        val intention = intentionService.create(IntentionBuilder().withUser(userCreate).build())
        val transaction = userService.beginTransaction(userAccept.id!!, intention.getId()!!)
        mockMvc.perform(
            MockMvcRequestBuilders.put("/users/cancelTransaction/{idTransaction}",
                transaction.id)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", header)
                .accept("application/json")
        ).andExpect(status().isOk)
    }

    @Test
    fun testCannotCancelATransactionWithAnInvalidTransactionId() {
        mockMvc.perform(
            MockMvcRequestBuilders.put("/users/cancelTransaction/{idTransaction}",
                "id")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", addHeader())
        ).andExpect(status().isBadRequest)
    }

    @Test
    fun testCannotCancelATransactionWithAnTransactionIdNotPersisted() {
        mockMvc.perform(
            MockMvcRequestBuilders.put("/users/cancelTransaction/{idTransaction}",
                -1)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", addHeader())
        ).andExpect(status().isNotFound)
    }

    private fun addHeader(): String {
        userService.create(UserBuilder().withEmail("defaultemail2@gmail.com")
            .withCVU("0011223344556677889911").withWallet("10254721").build())
        val login = LoginDTOBuilder().build()
        val response = mockMvc.perform(
            MockMvcRequestBuilders.post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(login))
        ).andExpect(status().isOk)

        val stringToken = response.andReturn().response.contentAsString
        return "Bearer ${stringToken.substring(10, stringToken.length - 2)}"
    }
}
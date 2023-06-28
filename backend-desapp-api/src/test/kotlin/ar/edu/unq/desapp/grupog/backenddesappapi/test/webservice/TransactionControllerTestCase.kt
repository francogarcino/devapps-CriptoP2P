package ar.edu.unq.desapp.grupog.backenddesappapi.test.webservice

import ar.edu.unq.desapp.grupog.backenddesappapi.model.CryptoActiveName
import ar.edu.unq.desapp.grupog.backenddesappapi.service.*
import ar.edu.unq.desapp.grupog.backenddesappapi.service.impl.ExternalApisServiceImpl
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
class TransactionControllerTestCase {

    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var context: WebApplicationContext

    @Autowired
    private lateinit var userService: UserService
    @Autowired
    private lateinit var intentionService: IntentionService
    @Autowired
    private lateinit var transactionService: TransactionService
    @Autowired
    private lateinit var dataService: DataService
    @Autowired private lateinit var apisService : ExternalApisServiceImpl

    private val mapper = ObjectMapper()

    @BeforeEach
    fun setup() {
        dataService.deleteAll()
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build()
    }

    @Test
    fun testCreateAndReadTransaction() {
        val header = addHeader()
        val userCreate = userService.create(UserBuilder().build())
        val intention = intentionService.create(IntentionBuilder().withCryptoPrice(apisService.getCryptoPrice(CryptoActiveName.ALICEUSDT)).withUser(userCreate).build())
        mockMvc.perform(
            MockMvcRequestBuilders.post("/users/createTransaction/{idIntention}",
                intention.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", header)
        ).andExpect(status().isOk)
        val transaction = transactionService.readAll().first()
        mockMvc.perform(
            MockMvcRequestBuilders.get("/transactions/{id}", transaction.id)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", header)
        ).andExpect(status().isOk)
    }

    @Test
    fun testCannotCreateTransactionWithAnInvalidIntentionId() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/users/createTransaction/{idIntention}", "id")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", addHeader())
        ).andExpect(status().isBadRequest)
    }

    @Test
    fun testCannotCreateTransactionWithAnIntentionIdNotPersisted() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/users/createTransaction/{idIntention}", -1)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", addHeader())
        ).andExpect(status().isNotFound)
    }

    @Test
    fun testCannotReadTransactionWithAnInvalidId() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/transactions/{id}", "id")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", addHeader())
        ).andExpect(status().isBadRequest)
    }

    @Test
    fun testCannotReadTransactionWithAnIdNotPersisted() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/transactions/{id}", -1)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", addHeader())
        ).andExpect(status().isNotFound)
    }

    @Test
    fun testReadAllTransactions() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/transactions/")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", addHeader())
        ).andExpect(status().isOk)
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
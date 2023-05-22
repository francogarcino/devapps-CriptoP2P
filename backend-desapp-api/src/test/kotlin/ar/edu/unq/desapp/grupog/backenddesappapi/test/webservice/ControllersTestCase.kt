package ar.edu.unq.desapp.grupog.backenddesappapi.test.webservice

import ar.edu.unq.desapp.grupog.backenddesappapi.service.*
import ar.edu.unq.desapp.grupog.backenddesappapi.test.utils.IntentionDTOBuilder
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
class ControllersTestCase {

    private lateinit var mockMvc: MockMvc

    @Autowired private lateinit var context: WebApplicationContext

    @Autowired private lateinit var userService: UserService
    @Autowired private lateinit var intentionService: IntentionService
    @Autowired private lateinit var transactionService: TransactionService
    @Autowired private lateinit var dataService: DataService

    private val mapper = ObjectMapper()

    @BeforeEach
    fun setup() {
        dataService.deleteAll()
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build()
    }

    @Test
    fun testCreateAndReadUser_AndCreateIntentionWithThatUser() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(UserBuilder().build()))
        ).andExpect(status().isOk)
        val user = userService.readAll().first()
        mockMvc.perform(
            MockMvcRequestBuilders.get("/users/{id}", user.id)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
        mockMvc.perform(
            MockMvcRequestBuilders.post("/users/{id}/createIntention", user.id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(IntentionDTOBuilder().build()))
        ).andExpect(status().isOk)
    }

    @Test
    fun testCreateUser_CreateIntentionWithThatUserAndReadThisIntention() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(UserBuilder().build()))
        ).andExpect(status().isOk)
        val user = userService.readAll().first()
        mockMvc.perform(
            MockMvcRequestBuilders.post("/users/{id}/createIntention", user.id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(IntentionDTOBuilder().build()))
        ).andExpect(status().isOk)
        val intention = intentionService.readAll().first()
        mockMvc.perform(
            MockMvcRequestBuilders.get("/intentions/{id}", intention.getId())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
    }

    @Test
    fun testCreateUsers_CreateIntentionWithAnUser_CreateAndReadTransaction() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(UserBuilder().build()))
        ).andExpect(status().isOk)
        mockMvc.perform(
            MockMvcRequestBuilders.post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(UserBuilder()
                    .withEmail("otroemail@gmail.com")
                    .withCVU("1212121212454545454578")
                    .withWallet("12344321").build()))
        ).andExpect(status().isOk)
        val users = userService.readAll()
        val userCreate = users[0]
        val userAccept = users[1]
        mockMvc.perform(
            MockMvcRequestBuilders.post("/users/{id}/createIntention", userCreate.id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(IntentionDTOBuilder().build()))
        ).andExpect(status().isOk)
        val intention = intentionService.readAll().first()
        mockMvc.perform(
            MockMvcRequestBuilders.post("/users/{idUser}/{idIntention}",
                userAccept.id, intention.getId())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
        val transaction = transactionService.readAll().first()
        mockMvc.perform(
            MockMvcRequestBuilders.get("/transactions/{id}", transaction.id)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
    }

    @Test
    fun testCreateUsers_CreateIntentionWithAnUser_CreateTransactionAndSystemCancelsIt() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(UserBuilder().build()))
        ).andExpect(status().isOk)
        mockMvc.perform(
            MockMvcRequestBuilders.post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(UserBuilder()
                    .withEmail("otroemail@gmail.com")
                    .withCVU("1212121212454545454578")
                    .withWallet("12344321").build()))
        ).andExpect(status().isOk)
        val users = userService.readAll()
        val userCreate = users[0]
        val userAccept = users[1]
        mockMvc.perform(
            MockMvcRequestBuilders.post("/users/{id}/createIntention", userCreate.id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(IntentionDTOBuilder().build()))
        ).andExpect(status().isOk)
        val intention = intentionService.readAll().first()
        mockMvc.perform(
            MockMvcRequestBuilders.post("/users/{idUser}/{idIntention}",
                userAccept.id, intention.getId())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
        val transaction = transactionService.readAll().first()
        mockMvc.perform(
            MockMvcRequestBuilders.put("/transactions/cancelTransaction/{idTransaction}", transaction.id)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
    }

    // @AfterEach fun teardown() { dataService.deleteAll() }
}
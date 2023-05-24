package ar.edu.unq.desapp.grupog.backenddesappapi.test.webservice

import ar.edu.unq.desapp.grupog.backenddesappapi.service.*
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

    @BeforeEach
    fun setup() {
        dataService.deleteAll()
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build()
    }

    @Test
    fun testCreateAndReadTransaction() {
        val userCreate = userService.create(UserBuilder().build())
        val userAccept = userService.create(UserBuilder()
            .withEmail("otroemail@gmail.com")
            .withCVU("1212121212454545454578")
            .withWallet("12344321").build())
        val intention = intentionService.create(IntentionBuilder().withUser(userCreate).build())
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
    fun testCannotCreateTransactionWithAnInvalidUserId() {
        val userCreate = userService.create(UserBuilder().build())
        val intention = intentionService.create(IntentionBuilder().withUser(userCreate).build())
        mockMvc.perform(
            MockMvcRequestBuilders.post("/users/{idUser}/{idIntention}", "id", intention.getId())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest)
    }

    @Test
    fun testCannotCreateTransactionWithAnUserIdNotPersisted() {
        val userCreate = userService.create(UserBuilder().build())
        val intention = intentionService.create(IntentionBuilder().withUser(userCreate).build())
        mockMvc.perform(
            MockMvcRequestBuilders.post("/users/{idUser}/{idIntention}", -1, intention.getId())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound)
    }

    @Test
    fun testCannotReadTransactionWithAnInvalidId() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/transactions/{id}", "id")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest)
    }

    @Test
    fun testCannotReadTransactionWithAnIdNotPersisted() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/transactions/{id}", -1)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound)
    }

    @Test
    fun testReadAllTransactions() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/transactions/")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
    }

    @Test
    fun testCreateTransactionAndSystemCancelsIt() {
        val userCreate = userService.create(UserBuilder().build())
        val userAccept = userService.create(UserBuilder()
            .withEmail("otroemail@gmail.com")
            .withCVU("1212121212454545454578")
            .withWallet("12344321").build())
        val intention = intentionService.create(IntentionBuilder().withUser(userCreate).build())
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

    @Test
    fun testSystemCannotCancelTransactionWithAnInvalidId() {
        mockMvc.perform(
            MockMvcRequestBuilders.put("/transactions/cancelTransaction/{idTransaction}", "id")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest)
    }

    @Test
    fun testSystemCannotCancelTransactionWithAnIdNotPersisted() {
        mockMvc.perform(
            MockMvcRequestBuilders.put("/transactions/cancelTransaction/{idTransaction}", -1)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound)
    }

}
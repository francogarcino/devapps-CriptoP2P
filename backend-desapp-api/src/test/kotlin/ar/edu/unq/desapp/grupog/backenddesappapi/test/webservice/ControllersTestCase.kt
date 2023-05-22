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
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build()
    }

    @Test
    fun testCreateAndReadUser_AndCreateIntentionWithThatUser() {
        val user = userService.create(UserBuilder().build())
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

    @AfterEach
    fun teardown() {
        dataService.deleteAll()
    }
}
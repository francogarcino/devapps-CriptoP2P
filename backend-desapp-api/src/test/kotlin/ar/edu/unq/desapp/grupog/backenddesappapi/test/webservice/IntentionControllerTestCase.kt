package ar.edu.unq.desapp.grupog.backenddesappapi.test.webservice

import ar.edu.unq.desapp.grupog.backenddesappapi.service.DataService
import ar.edu.unq.desapp.grupog.backenddesappapi.service.IntentionService
import ar.edu.unq.desapp.grupog.backenddesappapi.service.UserService
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
class IntentionControllerTestCase {

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
    fun testCreateAndReadIntention() {
        val user = userService.create(UserBuilder().build())
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
    fun testCannotCreateIntentionWithAnInvalidUserId() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/users/{id}/createIntention", "id")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(IntentionDTOBuilder().build()))
        ).andExpect(status().isBadRequest)
    }

    @Test
    fun testCannotCreateIntentionWithAnUserIdNotPersisted() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/users/{id}/createIntention", -1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(IntentionDTOBuilder().build()))
        ).andExpect(status().isNotFound)
    }

    @Test
    fun testCannotReadIntentionWithAnInvalidId() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/intentions/{id}", "id")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest)
    }

    @Test
    fun testCannotReadIntentionWithAnIdNotPersisted() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/intentions/{id}", -1)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound)
    }

    @Test
    fun testReadAllIntentions() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/intentions/")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
    }

    @Test
    fun testReadAllActiveIntentions() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/intentions/activeIntentions")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
    }

}
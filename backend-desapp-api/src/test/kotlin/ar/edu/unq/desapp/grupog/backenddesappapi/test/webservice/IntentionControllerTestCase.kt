package ar.edu.unq.desapp.grupog.backenddesappapi.test.webservice

import ar.edu.unq.desapp.grupog.backenddesappapi.service.DataService
import ar.edu.unq.desapp.grupog.backenddesappapi.service.IntentionService
import ar.edu.unq.desapp.grupog.backenddesappapi.service.UserService
import ar.edu.unq.desapp.grupog.backenddesappapi.test.utils.IntentionCreateDTOBuilder
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
        val header = addHeader()
        mockMvc.perform(
            MockMvcRequestBuilders.post("/users/createIntention")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(IntentionCreateDTOBuilder().build()))
                .header("Authorization", header)
        ).andExpect(status().isOk)
        val intention = intentionService.readAll().first()
        mockMvc.perform(
            MockMvcRequestBuilders.get("/intentions/{id}", intention.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", header)
        ).andExpect(status().isOk)
    }

    @Test
    fun testCannotReadIntentionWithAnInvalidId() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/intentions/{id}", "id")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", addHeader())
        ).andExpect(status().isBadRequest)
    }

    @Test
    fun testCannotReadIntentionWithAnIdNotPersisted() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/intentions/{id}", -1)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", addHeader())
        ).andExpect(status().isNotFound)
    }

    @Test
    fun testReadAllIntentions() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/intentions/")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", addHeader())
        ).andExpect(status().isOk)
    }

    @Test
    fun testReadAllActiveIntentions() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/intentions/activeIntentions")
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
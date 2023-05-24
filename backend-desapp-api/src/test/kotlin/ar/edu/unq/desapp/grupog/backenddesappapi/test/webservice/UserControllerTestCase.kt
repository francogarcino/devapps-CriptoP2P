package ar.edu.unq.desapp.grupog.backenddesappapi.test.webservice

import ar.edu.unq.desapp.grupog.backenddesappapi.service.DataService
import ar.edu.unq.desapp.grupog.backenddesappapi.service.UserService
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
import java.time.LocalDateTime

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserControllerTestCase {

    private lateinit var mockMvc: MockMvc

    @Autowired private lateinit var context: WebApplicationContext

    @Autowired private lateinit var userService: UserService
    @Autowired private lateinit var dataService: DataService

    private val mapper = ObjectMapper()

    @BeforeEach
    fun setup() {
        dataService.deleteAll()
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build()
    }

    @Test
    fun testCreateAndReadUser() {
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
    }

    @Test
    fun testCannotReadUserWithAnInvalidId() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/users/{id}", "id")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest)
    }

    @Test
    fun testCannotReadUserWithAnIdNotPersisted() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/users/{id}", -1)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound)
    }

    @Test
    fun testReadAllUsers() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/users/")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
    }

    @Test
    fun testGetAllUsersWithStats() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/users/stats")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
    }

    @Test
    fun testGetCryptoVolume() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(UserBuilder().build()))
        ).andExpect(status().isOk)
        val user = userService.readAll().first()
        mockMvc.perform(
            MockMvcRequestBuilders.get("/users/cryptoVolume/{userId}/{startDate}/{finishDate}",
                user.id,
                LocalDateTime.of(2023, 1, 1, 10, 0),
                LocalDateTime.now())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
    }

    @Test
    fun testCannotGetCryptoVolumeWithAnAnInvalidUserId() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/users/cryptoVolume/{userId}/{startDate}/{finishDate}",
                "id",
                LocalDateTime.of(2023, 1, 1, 10, 0),
                LocalDateTime.now())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest)
    }

    @Test
    fun testCannotGetCryptoVolumeWithAnUserIdNotPersisted() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/users/cryptoVolume/{userId}/{startDate}/{finishDate}",
                -1,
                LocalDateTime.of(2023, 1, 1, 10, 0),
                LocalDateTime.now())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound)
    }

}
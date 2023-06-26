package ar.edu.unq.desapp.grupog.backenddesappapi.test.webservice

import ar.edu.unq.desapp.grupog.backenddesappapi.service.DataService
import ar.edu.unq.desapp.grupog.backenddesappapi.service.UserService
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
        val cookie = addCookie()
        mockMvc.perform(
            MockMvcRequestBuilders.post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(UserBuilder().build()))
        ).andExpect(status().isOk)
        val user = userService.readAll().first()
        mockMvc.perform(
            MockMvcRequestBuilders.get("/users/{id}", user.id)
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(cookie)
        ).andExpect(status().isOk)
    }

    @Test
    fun testCannotReadUserWithAnInvalidId() {
        val cookie = addCookie()
        mockMvc.perform(
            MockMvcRequestBuilders.get("/users/{id}", "id")
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(cookie)
        ).andExpect(status().isBadRequest)
    }

    @Test
    fun testCannotReadUserWithAnIdNotPersisted() {
        val cookie = addCookie()
        mockMvc.perform(
            MockMvcRequestBuilders.get("/users/{id}", -1)
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(cookie)
        ).andExpect(status().isNotFound)
    }

    @Test
    fun testReadAllUsers() {
        val cookie = addCookie()
        mockMvc.perform(
            MockMvcRequestBuilders.get("/users/")
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(cookie)
        ).andExpect(status().isOk)
    }

    @Test
    fun testGetAllUsersWithStats() {
        val cookie = addCookie()
        mockMvc.perform(
            MockMvcRequestBuilders.get("/users/stats")
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(cookie)
        ).andExpect(status().isOk)
    }

    @Test
    fun testGetCryptoVolume() {
        val cookie = addCookie()
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
                .cookie(cookie)
        ).andExpect(status().isOk)
    }

    @Test
    fun testCannotGetCryptoVolumeWithAnAnInvalidUserId() {
        val cookie = addCookie()
        mockMvc.perform(
            MockMvcRequestBuilders.get("/users/cryptoVolume/{userId}/{startDate}/{finishDate}",
                "id",
                LocalDateTime.of(2023, 1, 1, 10, 0),
                LocalDateTime.now())
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(cookie)
        ).andExpect(status().isBadRequest)
    }

    @Test
    fun testCannotGetCryptoVolumeWithAnUserIdNotPersisted() {
        val cookie = addCookie()
        mockMvc.perform(
            MockMvcRequestBuilders.get("/users/cryptoVolume/{userId}/{startDate}/{finishDate}",
                -1,
                LocalDateTime.of(2023, 1, 1, 10, 0),
                LocalDateTime.now())
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
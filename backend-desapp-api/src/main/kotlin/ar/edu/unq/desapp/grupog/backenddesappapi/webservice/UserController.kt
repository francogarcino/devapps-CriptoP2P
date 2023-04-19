package ar.edu.unq.desapp.grupog.backenddesappapi.webservice

import ar.edu.unq.desapp.grupog.backenddesappapi.model.User
import ar.edu.unq.desapp.grupog.backenddesappapi.service.UserService
import ar.edu.unq.desapp.grupog.backenddesappapi.webservice.dtos.UserDTO
import ar.edu.unq.desapp.grupog.backenddesappapi.webservice.mappers.UserMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.lang.Exception

@RestController
@CrossOrigin
@RequestMapping("/users")
class UserController {
    @Autowired private lateinit var userService: UserService
    private var mapper = UserMapper()

    @GetMapping("/{id}")
    fun getUser(@PathVariable id: Long) : ResponseEntity<Any>{
        return try {
            ResponseEntity.ok().body(mapper.fromUserToDTO(userService.read(id)))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(e.message)
        }
    }

    @GetMapping("/")
    fun getAllUsers() : ResponseEntity<List<UserDTO>>{
        val users = userService.readAll()
        return ResponseEntity.ok(users.map { u -> mapper.fromUserToDTO(u) })
    }

    @PostMapping("/register")
    fun createUser(
        @RequestBody user: User
    ) : ResponseEntity<Any> {
        return try {
            val dto = mapper.fromUserToDTO(userService.create(user))
            ResponseEntity.ok().body(dto)
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(e.message)
        }
    }

    @PostMapping("/update")
    fun updateUser(
        @RequestBody user: User
    ) : ResponseEntity<Any> {
        return try {
            val dto = mapper.fromUserToDTO(userService.update(user))
            ResponseEntity.ok().body(dto)
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(e.message)
        }
    }

}
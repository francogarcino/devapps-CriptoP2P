package ar.edu.unq.desapp.grupog.backenddesappapi.persistence

import ar.edu.unq.desapp.grupog.backenddesappapi.model.User
import jakarta.persistence.Tuple
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface UserDAO : JpaRepository<User, Long> {

    fun findByEmail(email: String): Optional<User>

    @Query("select count(u) = 1 from UserApp u where u.email = ?1")
    fun existsByEmail(email: String): Boolean

    @Query("select count(u) = 1 from UserApp u where u.cvu = ?1")
    fun existsByCVU(cvu: String): Boolean

    @Query("select count(u) = 1 from UserApp u where u.wallet = ?1")
    fun existsByWallet(wallet: String): Boolean

    @Query(
        """
            SELECT u, (SELECT COUNT(t)
                       FROM Transaction t
                       WHERE (t.intention.user.id = u.id OR t.user_whoAccept.id = u.id)
                       AND t.status = "DONE")
                       FROM UserApp u
                       GROUP BY u
        """
    )
    fun getUsers(): List<Tuple>
}
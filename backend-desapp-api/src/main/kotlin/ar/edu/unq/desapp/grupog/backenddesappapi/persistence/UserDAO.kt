package ar.edu.unq.desapp.grupog.backenddesappapi.persistence

import ar.edu.unq.desapp.grupog.backenddesappapi.model.User
import jakarta.persistence.Tuple
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface UserDAO : JpaRepository<User, Long> {
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
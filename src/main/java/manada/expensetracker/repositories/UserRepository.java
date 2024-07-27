package manada.expensetracker.repositories;

import manada.expensetracker.entities.User;
import manada.expensetracker.entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findFirstByEmail(String email) ;

    User findByUserRole(UserRole userRole);

    User findByEmail(String email);
}

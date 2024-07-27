package manada.expensetracker.repositories;

import manada.expensetracker.entities.Expense;
import manada.expensetracker.entities.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByUserId(Long userId);
    Optional<Expense> findByTitle(String title);
}

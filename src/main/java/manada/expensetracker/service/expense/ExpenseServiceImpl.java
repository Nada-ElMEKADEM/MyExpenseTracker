package manada.expensetracker.service.expense;


import lombok.RequiredArgsConstructor;
import manada.expensetracker.dto.ExpenseDto;
import manada.expensetracker.dto.IncomeDto;
import manada.expensetracker.entities.Expense;
import manada.expensetracker.entities.Income;
import manada.expensetracker.repositories.ExpenseRepository;
import manada.expensetracker.repositories.IncomeRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {
    private final ExpenseRepository expenseRepository;

   @Override
    public List<Expense> getUserExpense(Long userId) {
        return expenseRepository.findByUserId(userId);
    }

   @Override
    public Expense saveExpense( Expense expense){
        return expenseRepository.save(expense);
    }



    public List<ExpenseDto> getAllExpensesByUser(Long userId){
        return expenseRepository.findByUserId(userId).stream()
                .sorted(Comparator.comparing(Expense::getDate).reversed())
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    public ExpenseDto convertToDto(Expense expense) {
        ExpenseDto dto = new ExpenseDto();
        dto.setTitle(expense.getTitle());
        dto.setAmount(expense.getAmount());
        dto.setDate(expense.getDate());
        dto.setCategory(expense.getCategory());
        dto.setDescription(expense.getDescription());
        return dto;
    }
    public ExpenseDto updateExpense(String title, ExpenseDto expenseDto) {
        Expense expense = expenseRepository.findByTitle(title)
                .orElseThrow(() -> new RuntimeException(" Expense not found"));
        if (expense != null) {
            expense.setTitle(expenseDto.getTitle());
            expense.setAmount(expenseDto.getAmount());
            expense.setDate(expenseDto.getDate());
            expense.setCategory(expenseDto.getCategory());
            expense.setDescription(expenseDto.getDescription());
            expenseRepository.save(expense);
            return convertToDto(expense);
        }
        return null; // Ou gérer selon vos besoins
    }

    public Long getExpenseIdByTitle(String title) {
        Expense expense = expenseRepository.findByTitle(title)
                .orElseThrow(() -> new RuntimeException("Expense not found"));
        return expense.getId();
    }

    public void deleteExpense(Long id) {
        expenseRepository.deleteById(id);
    }

    public Expense getExpenseById(Long id) {
        return expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

    }

    public Expense updateExpense(Long id, ExpenseDto expenseDto) {
        Expense existingExpense = getExpenseById(id);

        // Mettre à jour les champs de l'Income existant avec les valeurs du IncomeDto
        existingExpense.setTitle(expenseDto.getTitle());
        existingExpense.setAmount(expenseDto.getAmount());
        existingExpense.setDate(expenseDto.getDate());
        existingExpense.setCategory(expenseDto.getCategory());
        existingExpense.setDescription(expenseDto.getDescription());

        // Sauvegarder les modifications
        return expenseRepository.save(existingExpense);
    }

}

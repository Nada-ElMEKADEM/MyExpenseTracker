package manada.expensetracker.service.expense;

import manada.expensetracker.dto.ExpenseDto;
import manada.expensetracker.dto.IncomeDto;
import manada.expensetracker.entities.Expense;
import manada.expensetracker.entities.Income;

import java.util.List;

public interface ExpenseService {
    List<Expense> getUserExpense(Long userId);

    Expense saveExpense( Expense expense);

    // public List<IncomeDto> getAllIncomes();

    public List<ExpenseDto> getAllExpensesByUser(Long userId);
    public Long getExpenseIdByTitle(String title);
    public void deleteExpense(Long id);

    public Expense updateExpense(Long id, ExpenseDto expenseDto);
    public Expense getExpenseById(Long id) ;
    public ExpenseDto convertToDto(Expense expense);
}

package manada.expensetracker.controller;

import lombok.RequiredArgsConstructor;
import manada.expensetracker.dto.ExpenseDto;
import manada.expensetracker.dto.IncomeDto;
import manada.expensetracker.entities.Expense;
import manada.expensetracker.entities.Income;
import manada.expensetracker.entities.User;
import manada.expensetracker.service.expense.ExpenseService;
import manada.expensetracker.service.income.IncomeService;
import manada.expensetracker.service.jwt.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {
    private final ExpenseService expenseService;

    private final UserService userService;


    @GetMapping
    public ResponseEntity<List<ExpenseDto>> getUserExpenses(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername());
        List<Expense> expenses = expenseService.getUserExpense(user.getId());
        List<ExpenseDto> expenseDtos = expenses.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(expenseDtos);
    }

    @PostMapping
    public ResponseEntity<ExpenseDto> createExpense(@AuthenticationPrincipal UserDetails userDetails,
                                                  @RequestBody ExpenseDto expenseDto) {
        User user = userService.findByEmail(userDetails.getUsername());
        Expense expense = toEntity(expenseDto);
        expense.setUser(user); // Lier le revenu à l'utilisateur authentifié
        Expense savedExpense = expenseService.saveExpense(expense);
        ExpenseDto savedExpenseDto = toDto(savedExpense);
        return ResponseEntity.ok(savedExpenseDto);
    }
    /*@GetMapping("/all")
    public ResponseEntity<List<IncomeDto>> getAllIncomes() {
        List<IncomeDto> incomes = incomeService.getAllIncomes();
        return ResponseEntity.ok(incomes);
    }*/
    @GetMapping("/all/{userId}")
    public ResponseEntity<List<ExpenseDto>> getAllExpensesByUser(@PathVariable Long userId) {
        List<ExpenseDto> expenses = expenseService.getAllExpensesByUser(userId);
        return ResponseEntity.ok(expenses);
    }

    @GetMapping("/id")
    public ResponseEntity<Long> getExpenseIdByTitle(@RequestParam String title) {
        Long id = expenseService.getExpenseIdByTitle(title);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseDto> updateExpense(@PathVariable Long id, @RequestBody ExpenseDto expenseDto) {
        Expense updatedExpense = expenseService.updateExpense(id, expenseDto);
        return ResponseEntity.ok(expenseService.convertToDto(updatedExpense));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseDto> getExpenseById(@PathVariable Long id) {
        // Logique pour obtenir l'objet Income
        Expense expense = expenseService.getExpenseById(id);

        if (expense == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Convertir Income en IncomeDto
        ExpenseDto expenseDto = toDto(expense);

        return new ResponseEntity<>(expenseDto, HttpStatus.OK);
    }
    private ExpenseDto toDto(Expense expense) {
        ExpenseDto dto = new ExpenseDto();
        dto.setTitle(expense.getTitle());
        dto.setAmount(expense.getAmount());
        dto.setDate(expense.getDate());
        dto.setCategory(expense.getCategory());
        dto.setDescription(expense.getDescription());
        dto.setUserId(expense.getUser().getId());  // Associez le champ userId
        return dto;
    }

    private Expense toEntity(ExpenseDto expenseDto) {
        Expense  expense = new Expense();
        expense.setTitle(expenseDto.getTitle());
        expense.setAmount(expenseDto.getAmount());
        expense.setDate(expenseDto.getDate());
        expense.setCategory(expenseDto.getCategory());
        expense.setDescription(expenseDto.getDescription());
        // L'association de l'utilisateur se fait dans createIncome
        return expense;
    }



















}

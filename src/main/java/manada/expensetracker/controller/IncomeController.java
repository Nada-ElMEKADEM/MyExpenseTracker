package manada.expensetracker.controller;

import lombok.RequiredArgsConstructor;
import manada.expensetracker.dto.IncomeDto;
import manada.expensetracker.entities.Income;
import manada.expensetracker.entities.User;
import manada.expensetracker.service.income.IncomeService;
import manada.expensetracker.service.jwt.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/incomes")
@RequiredArgsConstructor
public class IncomeController {


    private final IncomeService incomeService;

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<IncomeDto>> getUserIncomes(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername());
        List<Income> incomes = incomeService.getUserIncomes(user.getId());
        List<IncomeDto> incomeDtos = incomes.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(incomeDtos);
    }

    @PostMapping
    public ResponseEntity<IncomeDto> createIncome(@AuthenticationPrincipal UserDetails userDetails,
                                                  @RequestBody IncomeDto incomeDto) {
        User user = userService.findByEmail(userDetails.getUsername());
        Income income = toEntity(incomeDto);
        income.setUser(user); // Lier le revenu à l'utilisateur authentifié
        Income savedIncome = incomeService.saveIncome(income);
        IncomeDto savedIncomeDto = toDto(savedIncome);
        return ResponseEntity.ok(savedIncomeDto);
    }
    /*@GetMapping("/all")
    public ResponseEntity<List<IncomeDto>> getAllIncomes() {
        List<IncomeDto> incomes = incomeService.getAllIncomes();
        return ResponseEntity.ok(incomes);
    }*/
    @GetMapping("/all/{userId}")
    public ResponseEntity<List<IncomeDto>> getAllIncomesByUser(@PathVariable Long userId) {
        List<IncomeDto> incomes = incomeService.getAllIncomesByUser(userId);
        return ResponseEntity.ok(incomes);
    }

    @GetMapping("/id")
    public ResponseEntity<Long> getIncomeIdByTitle(@RequestParam String title) {
        Long id = incomeService.getIncomeIdByTitle(title);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncome(@PathVariable Long id) {
        incomeService.deleteIncome(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<IncomeDto> updateIncome(@PathVariable Long id, @RequestBody IncomeDto incomeDto) {
        Income updatedIncome = incomeService.updateIncome(id, incomeDto);
        return ResponseEntity.ok(incomeService.convertToDto(updatedIncome));
    }

    @GetMapping("/{id}")
    public ResponseEntity<IncomeDto> getIncomeById(@PathVariable Long id) {
        // Logique pour obtenir l'objet Income
        Income income = incomeService.getIncomeById(id);

        if (income == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Convertir Income en IncomeDto
        IncomeDto incomeDto = toDto(income);

        return new ResponseEntity<>(incomeDto, HttpStatus.OK);
    }
    private IncomeDto toDto(Income income) {
        IncomeDto dto = new IncomeDto();
        dto.setTitle(income.getTitle());
        dto.setAmount(income.getAmount());
        dto.setDate(income.getDate());
        dto.setCategory(income.getCategory());
        dto.setDescription(income.getDescription());
        dto.setUserId(income.getUser().getId());  // Associez le champ userId
        return dto;
    }

    private Income toEntity(IncomeDto incomeDto) {
        Income income = new Income();
        income.setTitle(incomeDto.getTitle());
        income.setAmount(incomeDto.getAmount());
        income.setDate(incomeDto.getDate());
        income.setCategory(incomeDto.getCategory());
        income.setDescription(incomeDto.getDescription());
        // L'association de l'utilisateur se fait dans createIncome
        return income;
    }



}

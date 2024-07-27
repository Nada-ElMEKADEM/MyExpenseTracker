package manada.expensetracker.service.income;


import lombok.RequiredArgsConstructor;
import manada.expensetracker.dto.IncomeDto;
import manada.expensetracker.entities.Income;
import manada.expensetracker.repositories.IncomeRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IncomeServiceImpl implements IncomeService {
    private final IncomeRepository incomeRepository;

    @Override
    public List<Income> getUserIncomes(Long userId) {
        return incomeRepository.findByUserId(userId);
    }

    @Override
    public Income saveIncome(Income income) {
        return incomeRepository.save(income);
    }

    public List<IncomeDto> getAllIncomes() {
        return incomeRepository.findAll().stream()
                .sorted(Comparator.comparing(Income::getDate).reversed())
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<IncomeDto> getAllIncomesByUser(Long userId) {
        return incomeRepository.findByUserId(userId).stream()
                .sorted(Comparator.comparing(Income::getDate).reversed())
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    public IncomeDto convertToDto(Income income) {
        IncomeDto dto = new IncomeDto();
        dto.setTitle(income.getTitle());
        dto.setAmount(income.getAmount());
        dto.setDate(income.getDate());
        dto.setCategory(income.getCategory());
        dto.setDescription(income.getDescription());
        return dto;
    }
    public IncomeDto updateIncome(String title, IncomeDto incomeDto) {
        Income income = incomeRepository.findByTitle(title)
                .orElseThrow(() -> new RuntimeException("Income not found"));
        if (income != null) {
            income.setTitle(incomeDto.getTitle());
            income.setAmount(incomeDto.getAmount());
            income.setDate(incomeDto.getDate());
            income.setCategory(incomeDto.getCategory());
            income.setDescription(incomeDto.getDescription());
            incomeRepository.save(income);
            return convertToDto(income);
        }
        return null; // Ou gérer selon vos besoins
    }

    public Long getIncomeIdByTitle(String title) {
        Income income = incomeRepository.findByTitle(title)
                .orElseThrow(() -> new RuntimeException("Income not found"));
        return income.getId();
    }

    public void deleteIncome(Long id) {
        incomeRepository.deleteById(id);
    }

    public Income getIncomeById(Long id) {
        return incomeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Income not found"));

    }

    public Income updateIncome(Long id, IncomeDto incomeDto) {
        Income existingIncome = getIncomeById(id);

        // Mettre à jour les champs de l'Income existant avec les valeurs du IncomeDto
        existingIncome.setTitle(incomeDto.getTitle());
        existingIncome.setAmount(incomeDto.getAmount());
        existingIncome.setDate(incomeDto.getDate());
        existingIncome.setCategory(incomeDto.getCategory());
        existingIncome.setDescription(incomeDto.getDescription());

        // Sauvegarder les modifications
        return incomeRepository.save(existingIncome);
    }



}


package manada.expensetracker.service.income;


import manada.expensetracker.dto.IncomeDto;
import manada.expensetracker.entities.Income;

import java.util.List;
import java.util.Optional;

public interface IncomeService {
    List<Income> getUserIncomes(Long userId);

    Income saveIncome(Income income);

   // public List<IncomeDto> getAllIncomes();

    public List<IncomeDto> getAllIncomesByUser(Long userId);
    public Long getIncomeIdByTitle(String title);
    public void deleteIncome(Long id);

    public Income updateIncome(Long id, IncomeDto incomeDto);
    public Income getIncomeById(Long id) ;
    public IncomeDto convertToDto(Income income);



}
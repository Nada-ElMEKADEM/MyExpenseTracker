package manada.expensetracker.entities;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name="incomes")
public class Income {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;
    private Integer amount ;
    private LocalDate date;
    private String category;
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;



}

package manada.expensetracker.dto;

import lombok.Data;
import manada.expensetracker.entities.UserRole;

@Data
public class AuthenticationResponse {
    private String jwt;
    private UserRole userRole;
    private long userId;
}

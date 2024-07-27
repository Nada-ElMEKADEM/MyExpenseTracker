package manada.expensetracker.service.auth;

import manada.expensetracker.dto.SignupRequest;
import manada.expensetracker.dto.UserDto;

public interface AuthService {

    UserDto createUser(SignupRequest signupRequest);
    boolean hasUserWithEmail(String email);
}

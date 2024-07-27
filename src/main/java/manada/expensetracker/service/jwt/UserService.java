package manada.expensetracker.service.jwt;

import manada.expensetracker.entities.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {

    UserDetailsService userDetailsService();

    User findByEmail(String username);


    String findUserNameByEmail(String email);
}

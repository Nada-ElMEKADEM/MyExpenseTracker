package manada.expensetracker.service.auth;


import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import manada.expensetracker.dto.SignupRequest;
import manada.expensetracker.dto.UserDto;
import manada.expensetracker.entities.User;
import manada.expensetracker.entities.UserRole;
import manada.expensetracker.repositories.IncomeRepository;
import manada.expensetracker.repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static manada.expensetracker.entities.UserRole.USER;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final IncomeRepository incomeRepository;

    @PostConstruct
    public void createAdminAccount(){
        User  adminAccount= userRepository.findByUserRole(UserRole.ADMIN);
        if(adminAccount==null){
            User newAdminAccount= new User();
            newAdminAccount.setName("Admin");
            newAdminAccount.setEmail("admin@test.com");
            newAdminAccount.setPassword(new BCryptPasswordEncoder().encode("admin"));
            newAdminAccount.setUserRole(UserRole.ADMIN);
            userRepository.save(newAdminAccount);
            System.out.println("Admin account created Successfully");

        }
    }


    @Override
    public UserDto createUser(SignupRequest signupRequest) {
        User user=new User();
        user.setName(signupRequest.getName());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(new BCryptPasswordEncoder().encode(signupRequest.getPassword()));
        user.setUserRole(USER);
        User createdUser =userRepository.save(user);
        UserDto userDto=new UserDto();
        userDto.setId(createdUser.getId());
        return userDto;
    }


    @Override
    public boolean hasUserWithEmail(String email) {
        return userRepository.findFirstByEmail(email).isPresent();
    }
}

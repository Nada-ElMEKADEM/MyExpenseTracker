package manada.expensetracker.controller;

import lombok.RequiredArgsConstructor;
import manada.expensetracker.dto.AuthenticationRequest;
import manada.expensetracker.dto.AuthenticationResponse;
import manada.expensetracker.dto.SignupRequest;
import manada.expensetracker.dto.UserDto;
import manada.expensetracker.entities.User;
import manada.expensetracker.repositories.UserRepository;
import manada.expensetracker.service.auth.AuthService;
import manada.expensetracker.service.jwt.UserService;
import manada.expensetracker.utils.JWTUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor

public class AuthController {

    private final AuthService authService;

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;


    @PostMapping("/signup")
    public ResponseEntity<?> signupUser(@RequestBody SignupRequest signupRequest) {
        if(authService.hasUserWithEmail(signupRequest.getEmail()))
            return new ResponseEntity<>("Email already in use", HttpStatus.NOT_ACCEPTABLE);
        UserDto createdUserdto= authService.createUser(signupRequest);
        if(createdUserdto==null) {
            return new ResponseEntity<> ("User not Created", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>((createdUserdto), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public AuthenticationResponse createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws BadCredentialsException, DisabledException, UsernameNotFoundException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),authenticationRequest.getPassword()));
        }catch (BadCredentialsException e){
            throw new BadCredentialsException("Incorrect username or password");
        }
        final UserDetails userDetails=userService.userDetailsService().loadUserByUsername(authenticationRequest.getEmail());
        Optional<User> optionalUser=userRepository.findFirstByEmail(userDetails.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);
        AuthenticationResponse authenticationResponse =  new AuthenticationResponse();
        if(optionalUser.isPresent()){
            authenticationResponse.setJwt(jwt);
            authenticationResponse.setUserId(optionalUser.get().getId());
            authenticationResponse.setUserRole(optionalUser.get().getUserRole());

        }
        return authenticationResponse;
    }



}

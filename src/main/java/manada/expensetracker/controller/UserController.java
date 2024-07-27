package manada.expensetracker.controller;

import lombok.RequiredArgsConstructor;
import manada.expensetracker.dto.UserDto;
import manada.expensetracker.entities.User;
import manada.expensetracker.service.jwt.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor

public class UserController {

    private final UserService userService;



    @GetMapping("/id")
    public ResponseEntity<Long> getUserIdByEmail(@RequestParam String email) {
        User user = userService.findByEmail(email);
        if (user != null) {
            return ResponseEntity.ok(user.getId());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    

    @GetMapping("/name")
    public ResponseEntity<Map<String, String>> getUserNameByEmail(@RequestParam String email) {
        String userName = userService.findUserNameByEmail(email);
        if (userName != null) {
            Map<String, String> response = new HashMap<>();
            response.put("name", userName);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }



}

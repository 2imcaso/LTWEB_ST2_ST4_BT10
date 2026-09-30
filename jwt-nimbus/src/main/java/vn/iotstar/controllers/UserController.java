package vn.iotstar.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import vn.iotstar.entity.User;
import vn.iotstar.repository.UserRepository;
import java.util.List;

@RequestMapping("/users")
@RestController
public class UserController {
    private final UserRepository userRepository;
    public UserController(UserRepository userRepository) { this.userRepository = userRepository; }
    @GetMapping("/me") public ResponseEntity<User> authenticatedUser(@AuthenticationPrincipal User user) { return ResponseEntity.ok(user); }
    @GetMapping("/") public ResponseEntity<List<User>> allUsers() { return ResponseEntity.ok(userRepository.findAll()); }
}

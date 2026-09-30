package vn.iotstar.services;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import vn.iotstar.entity.User;
import vn.iotstar.models.LoginUserModel;
import vn.iotstar.models.RegisterUserModel;
import vn.iotstar.repository.UserRepository;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    public AuthenticationService(UserRepository userRepository, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager; this.userRepository = userRepository; this.passwordEncoder = passwordEncoder;
    }
    public User signup(RegisterUserModel input) {
        String email = input.getEmail().trim().toLowerCase();
        if (userRepository.existsByEmail(email)) throw new ResponseStatusException(HttpStatus.CONFLICT, "Email da ton tai");
        User user = new User(); user.setFullName(input.getFullName()); user.setEmail(email);
        user.setPassword(passwordEncoder.encode(input.getPassword())); user.setImages("https://placehold.co/160x160?text=User");
        return userRepository.save(user);
    }
    public User authenticate(LoginUserModel input) {
        String email = input.getEmail().trim().toLowerCase();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, input.getPassword()));
        return userRepository.findByEmail(email).orElseThrow();
    }
}

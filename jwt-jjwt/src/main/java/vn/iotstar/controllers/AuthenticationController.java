package vn.iotstar.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.iotstar.entity.User;
import vn.iotstar.models.LoginResponse;
import vn.iotstar.models.LoginUserModel;
import vn.iotstar.models.RegisterUserModel;
import vn.iotstar.services.AuthenticationService;
import vn.iotstar.services.JwtService;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService; private final AuthenticationService authenticationService;
    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) { this.jwtService = jwtService; this.authenticationService = authenticationService; }
    @PostMapping("/signup") public ResponseEntity<User> register(@Valid @RequestBody RegisterUserModel user) { return ResponseEntity.ok(authenticationService.signup(user)); }
    @PostMapping("/login") public ResponseEntity<LoginResponse> authenticate(@Valid @RequestBody LoginUserModel loginUser) {
        User authenticatedUser = authenticationService.authenticate(loginUser);
        return ResponseEntity.ok(new LoginResponse(jwtService.generateToken(authenticatedUser), jwtService.getExpirationTime()));
    }
}

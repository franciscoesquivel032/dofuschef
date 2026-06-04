package com.franciscoesquivel.dofuschef.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> authenticateUser(@RequestBody UserLoginDto user){
        try {
            return ResponseEntity.ok().body(this.authService.login(user));
        } catch (AuthenticationException e) {
            log.error(e.getMessage());
            return ResponseEntity
                    .badRequest()
                    .body(new LoginResponseDto("Failed to authenticate user with username: {user.getUsername()}", false));

        }
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponseDto> register(@RequestBody User user) {
        try {
            return ResponseEntity.ok().body(this.authService.register(user));
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return ResponseEntity
                    .badRequest()
                    .body(new LoginResponseDto("Failed to register user with username: {user.getUsername()}", false));
        }
    }
}

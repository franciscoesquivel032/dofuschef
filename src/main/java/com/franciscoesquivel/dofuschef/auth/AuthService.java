package com.franciscoesquivel.dofuschef.auth;

import com.franciscoesquivel.dofuschef.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final IUserRepository userRepository;
    private final PasswordEncoder encoder;

    @Autowired
    public AuthService(
            AuthenticationManager authenticationManager,
            JwtUtils jwtUtils,
            IUserRepository userRepository,
            PasswordEncoder encoder
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public LoginResponseDto login(UserLoginDto user) throws AuthenticationException {
        Authentication authentication = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = this.jwtUtils.generateToken(userDetails.getUsername());
        return new LoginResponseDto(token, true);
    }

    public LoginResponseDto register(User user) throws IllegalArgumentException {
        if(user == null)
            throw new IllegalArgumentException();
        if(this.userRepository.existsUserByUsername(user.getUsername()))
            throw new IllegalArgumentException("Username already in use");
        if(this.userRepository.existsUserByEmail(user.getEmail()))
            throw new IllegalArgumentException("Email already in use");

        String encryptedPw = this.encoder.encode(user.getPassword());

       this.userRepository.save(
               User.builder().
               email(user.getEmail()).
               username(user.getUsername()).
               password(encryptedPw).
               build()
       );

       String token = this.jwtUtils.generateToken(user.getUsername());

       return new LoginResponseDto(token, true);
    }
}

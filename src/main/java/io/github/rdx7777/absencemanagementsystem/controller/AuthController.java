package io.github.rdx7777.absencemanagementsystem.controller;

import io.github.rdx7777.absencemanagementsystem.payload.request.LoginRequest;
import io.github.rdx7777.absencemanagementsystem.payload.response.JwtResponse;
import io.github.rdx7777.absencemanagementsystem.repository.UserRepository;
import io.github.rdx7777.absencemanagementsystem.security.jwt.JwtUtils;
import io.github.rdx7777.absencemanagementsystem.security.services.UserDetailsImpl;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = {"https://a-m-system.herokuapp.com/", "*"}, maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    final
    AuthenticationManager authenticationManager;

    final
    UserRepository userRepository;

    final
    PasswordEncoder encoder;

    final
    JwtUtils jwtUtils;

    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository,
                          PasswordEncoder encoder, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        System.out.println("username: " + loginRequest.getUsername());

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
            .map(item -> item.getAuthority())
            .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
            userDetails.getId(),
            userDetails.getUsername(),
            roles));
    }

}

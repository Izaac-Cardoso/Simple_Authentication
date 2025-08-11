package com.cardoso_izaac.SimpleAuthentication.controllers;

import com.cardoso_izaac.SimpleAuthentication.configuration.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import com.cardoso_izaac.SimpleAuthentication.domain.service.UserService;
import com.cardoso_izaac.SimpleAuthentication.dto.SignUpDTO;
import com.cardoso_izaac.SimpleAuthentication.dto.SignInDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users/v1")
public class AuthController {

    private final UserService service;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;

    public AuthController(UserService service, AuthenticationManager authManager, JwtService jwtService) {
        this.service = service;
        this.authManager = authManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody SignUpDTO request) {
        service.createUser(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/signin")
    public ResponseEntity<String> signin(@Valid @RequestBody SignInDTO request) {
        Authentication auth;
        try{ auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.name(), request.password()));
                SecurityContextHolder.getContext().setAuthentication(auth);

        } catch(BadCredentialsException e) {
            e.getMessage();
       }

        String token = jwtService.generateToken(request);
        return ResponseEntity.ok(token);
    }
}

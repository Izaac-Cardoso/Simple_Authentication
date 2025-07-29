package com.cardoso_izaac.SimpleAuthentication.controllers;

import com.cardoso_izaac.SimpleAuthentication.domain.service.UserService;
import com.cardoso_izaac.SimpleAuthentication.dto.SignUpDTO;
import com.cardoso_izaac.SimpleAuthentication.dto.SignInDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users/v1")
public class AuthController {

    private final UserService service;

    public AuthController(UserService service) {
        this.service = service;
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody SignUpDTO request) {
        service.createUser(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/signin")
    public ResponseEntity<Void> signin(@Valid @RequestBody SignInDTO request) {
        service.login(request);
        return ResponseEntity.ok().build();
    }
}

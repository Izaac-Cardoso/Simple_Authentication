package com.cardoso_izaac.SimpleAuthentication.domain.service;

import com.cardoso_izaac.SimpleAuthentication.domain.models.User;
import com.cardoso_izaac.SimpleAuthentication.domain.repositories.UserRepository;
import com.cardoso_izaac.SimpleAuthentication.dto.SignUpDTO;
import com.cardoso_izaac.SimpleAuthentication.exceptions.BadCredentialsException;
import com.cardoso_izaac.SimpleAuthentication.exceptions.DuplicatedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.function.Predicate;

@Service
public class UserService {

    private final UserRepository repository;
    private final AuthenticationManager authManager;
    private final PasswordEncoder encoder;

    public UserService(UserRepository repository, AuthenticationManager authManager, PasswordEncoder encoder) {
        this.repository = repository;
        this.authManager = authManager;
        this.encoder = encoder;
    }

    @Transactional
    public ResponseEntity<?> createUser(SignUpDTO request) {

        Predicate<String> notValid = (credential) -> credential == null || credential.isBlank();

        // Map the fields to their respective values passed in request
        Map<String, String> fields = Map.of(
                "name", request.name(),
                "password", request.password(),
                "email", request.email());

        // check if there's an invalid field
        fields.entrySet().stream()
                .filter(input -> notValid.test(input.getValue()))
                .findFirst()
                .ifPresent(input -> {
                    throw new BadCredentialsException("Invalid field: " + input.getKey());
                });

        var email = request.email();
        var currentUser = repository.findByEmail(email);
        if(currentUser.isPresent()) {
           throw new DuplicatedException(String.format("the e-mail %s already exists", email));
        }

        String hashedPassword = encoder.encode(request.password());

        User newUser = new User(request.name(), request.email(), hashedPassword);
        repository.save(newUser);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}

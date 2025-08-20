package com.cardoso_izaac.SimpleAuthentication.domain.service;

import com.cardoso_izaac.SimpleAuthentication.domain.models.User;
import com.cardoso_izaac.SimpleAuthentication.domain.repositories.UserRepository;
import com.cardoso_izaac.SimpleAuthentication.dto.SignUpDTO;
import com.cardoso_izaac.SimpleAuthentication.dto.SignInDTO;
import com.cardoso_izaac.SimpleAuthentication.exceptions.DuplicatedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseStatus;

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
        if(request.name() == null || request.name().isBlank()
                || request.password() == null || request.password().isBlank()
                || request.email() == null || request.email().isBlank()) {
            throw new BadCredentialsException("One or more invalid fields.");
        }

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

//    public String login(SignInDTO request) {
//        Authentication auth;
//        try{ auth = authManager.authenticate(
//                new UsernamePasswordAuthenticationToken(request.name(), request.password()));
//
//            SecurityContextHolder.getContext().setAuthentication(auth);
//
//        } catch(BadCredentialsException e) {
//            e.getMessage();
//        }
//    }
}

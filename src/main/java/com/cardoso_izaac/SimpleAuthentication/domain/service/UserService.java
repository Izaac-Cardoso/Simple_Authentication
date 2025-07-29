package com.cardoso_izaac.SimpleAuthentication.domain.service;

import com.cardoso_izaac.SimpleAuthentication.domain.models.User;
import com.cardoso_izaac.SimpleAuthentication.domain.repositories.UserRepository;
import com.cardoso_izaac.SimpleAuthentication.dto.SignUpDTO;
import com.cardoso_izaac.SimpleAuthentication.dto.SignInDTO;
import com.cardoso_izaac.SimpleAuthentication.exceptions.DuplicatedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public void createUser(SignUpDTO request) {
        var email = request.email();
        var currentUser = repository.findByEmail(email);
        if(currentUser.isPresent()) {
           throw new DuplicatedException(String.format("the e-mail %s already exists", email));
        }

        String hashedPassword = encoder.encode(request.password());

        User newUser = new User(request.name(), request.email(), hashedPassword);
        repository.save(newUser);
    }

    public void login(SignInDTO request) {
        Authentication auth;
        try{auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.name(), request.password()));

            SecurityContextHolder.getContext().setAuthentication(auth);

        } catch(BadCredentialsException e) {
            e.getMessage();
        }
    }
}

package com.cardoso_izaac.SimpleAuthentication.configuration;

import com.cardoso_izaac.SimpleAuthentication.domain.repositories.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsConfig implements UserDetailsService {

    private final UserRepository repository;

    public UserDetailsConfig(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
         var userObj = repository.findByEmail(email)
                  .orElseThrow(() -> new UsernameNotFoundException("The user informed was not found"));

         return User.builder()
                 .username(userObj.getName())
                 .password(userObj.getPassword())
                 .build();
    }
}

package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    // ✅ Spring Security uses this method
    @Override
    public UserDetails loadUserByUsername(String mobile) throws UsernameNotFoundException {
        User user = repo.findByMobile(mobile)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with mobile: " + mobile));

        // Spring Security User object implements UserDetails
        return new org.springframework.security.core.userdetails.User(
                user.getMobile(),             // username
                user.getPassword(),           // password (hashed)
                Collections.emptyList()       // authorities/roles (empty for now)
        );
    }

    // ✅ Helper methods for your controllers
    public Optional<User> findByMobile(String mobile){
        return repo.findByMobile(mobile);
    }

    public boolean existsByMobile(String mobile){
        return repo.existsByMobile(mobile);
    }
}

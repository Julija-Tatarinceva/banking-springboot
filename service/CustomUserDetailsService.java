package com.BankingApp.service;

import com.BankingApp.model.User;
import com.BankingApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        System.out.println("Trying to load user: " + email);
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(), // must be already BCrypt-encoded
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}

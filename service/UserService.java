package com.BankingApp.service;

import com.BankingApp.exception.EmailAlreadyInUseException;
import com.BankingApp.exception.UserNotFoundException;
import com.BankingApp.model.User;
import com.BankingApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository repository) {
        this.userRepository = repository;
    }

    public User findUserById(long id) {
        return userRepository.findById((int) id)
                .orElseThrow(() -> new UserNotFoundException("User not found by id: " + id));
    }
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found by email: " + email));
    }

    public User createAccount(String email, String rawPassword, String name) {
        userRepository.findByEmail(email).ifPresent(existing -> {
            throw new EmailAlreadyInUseException("Email already in use: " + email);
        });

        // For security purposes we do not store raw password, only the encrypted version
        String encryptedPassword = new BCryptPasswordEncoder().encode(rawPassword);
        User account = new User(email, encryptedPassword, name);
        return userRepository.save(account);
    }
}

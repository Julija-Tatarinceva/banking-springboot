package com.BankingApp.service;

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
                .orElseThrow(() -> new RuntimeException("User not found by id: " + id));
    }
    public Optional<User> findUserByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found by email: " + email)));
    }

//    public User authenticate(String email, String rawPassword) {
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("Account from not found"));
////        if(user.getPassword().equals(rawPassword)) {
////            return user;
////        }
//
//        if (encoder.matches(rawPassword, user.getPassword())) {
//            return user;
//        }
//        return null;
//    }
    public User createAccount(String email, String rawPassword, String name) {
        // For security purposes we do not store raw password, only the encrypted version
        String encryptedPassword = new BCryptPasswordEncoder().encode(rawPassword);
        User account = new User(email, encryptedPassword, name);
        return userRepository.save(account);
    }

}

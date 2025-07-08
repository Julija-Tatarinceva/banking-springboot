package com.BankingApp.service;

import com.BankingApp.model.User;
import com.BankingApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository repository) {
        this.userRepository = repository;
    }

    public User findUserById(long id) {
        return userRepository.findById((int) id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User authenticate(String email, String rawPassword) {
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Account from not found"));
        if(user.getPassword().equals(rawPassword)) {
            return user;
        }

//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        if (encoder.matches(rawPassword, user.getPassword())) {
//            return user;
//        }
        return null;
    }
    public User createAccount(String email, String password, String name) {
        User account = new User(email, password, name);
        return userRepository.save(account);
    }

}

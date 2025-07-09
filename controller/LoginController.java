package com.BankingApp.controller;

import com.BankingApp.dto.LoginRequest;
import com.BankingApp.dto.RegistrationRequest;
import com.BankingApp.model.User;
import com.BankingApp.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    private final UserService userService;
    final Logger logger = LogManager.getLogger(LoginController.class);

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginForm(HttpSession session, Model model) {
        model.addAttribute("user", new LoginRequest());
        String errorMessage = (String) session.getAttribute("LOGIN_ERROR");
        if (errorMessage != null) {
            model.addAttribute("loginError", errorMessage);
            session.removeAttribute("LOGIN_ERROR"); // Avoid showing it again after reload
        }
        return "login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("registrationRequest", new RegistrationRequest());
        return "register";
    }

    @PostMapping("/register")
    public String registerSubmit(@ModelAttribute RegistrationRequest registrationRequest, HttpSession session) {
        User newUser = userService.createAccount(registrationRequest.getEmail(), registrationRequest.getPassword(), registrationRequest.getName());
        session.setAttribute("userID", newUser.getId());
        return "redirect:/";
    }
}


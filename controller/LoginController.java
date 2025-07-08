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

@Controller
public class LoginController {
    private final UserService userService;
    final Logger logger = LogManager.getLogger(LoginController.class);

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("user", new LoginRequest());
        return "login";
    }

    @PostMapping("/login")
    public String loginSubmit(@ModelAttribute LoginRequest loginRequest, HttpSession session, Model model) {
        User user = userService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
        if (user == null) {
            model.addAttribute("error", "Invalid credentials");
            return "login";
        }

        session.setAttribute("userId", user.getId());
        return "redirect:/";
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

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId != null) {
            logger.info("User (id={}) logged out", userId);
        }
        session.invalidate();
        return "redirect:/login?logout";
    }
}


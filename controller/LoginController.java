package com.BankingApp.controller;

import com.BankingApp.dto.LoginRequest;
import com.BankingApp.dto.RegistrationRequest;
import com.BankingApp.model.BankAccount;
import com.BankingApp.service.BankService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {


    private final BankService accountService;

    @Autowired
    public LoginController(BankService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("account", new LoginRequest());
        return "login";
    }

    @PostMapping("/login")
    public String loginSubmit(@ModelAttribute LoginRequest loginRequest, HttpSession session, Model model) {
        boolean authOK = accountService.authenticate(loginRequest.getAccountNumber(), loginRequest.getPassword());
        if(authOK) {
            session.setAttribute("accountID", loginRequest.getAccountNumber());
            return "redirect:/";
        }else {
            model.addAttribute("error", "Invalid account number or password");
            model.addAttribute("account", new LoginRequest());

            return "login";
        }
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("account", new RegistrationRequest());
        return "register";
    }

    @PostMapping("/register")
    public String registerSubmit(@ModelAttribute RegistrationRequest registrationRequest, HttpSession session, Model model) {
//        boolean authOK = accountService.authenticate(registrationRequest.getAccountNumber(), registrationRequest.getPassword());
//        if(authOK) {
        BankAccount newAccount = accountService.createAccount(registrationRequest.getPassword(), registrationRequest.getInitialBalance());
        System.out.println(registrationRequest.getInitialBalance());
        session.setAttribute("accountID", newAccount.getAccountNumber());
        return "redirect:/";
    }
}


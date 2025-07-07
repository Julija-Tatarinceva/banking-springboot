package com.BankingApp.controller;

import com.BankingApp.dto.AccountRequest;
import com.BankingApp.model.Transaction;
import com.BankingApp.model.BankAccount;
import com.BankingApp.service.BankService;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class BankController {

    private final BankService service;
    final Logger logger = LogManager.getLogger(BankController.class);

    public BankController(BankService service) {
        this.service = service;
    }


    @GetMapping("/")
    public String showMenu(HttpSession session, Model model) {
        BankAccount account = (BankAccount) session.getAttribute("account");
        if (account == null) {
            return "redirect:/login";
        }
        model.addAttribute("account", account);
        return "menu";
    }
    @GetMapping("/logout")
    public String logoutScreen(HttpSession session, Model model) {
        BankAccount account = (BankAccount) session.getAttribute("account");

        model.addAttribute("account", null);
        return "redirect:/login";
    }
    @PostMapping("/logout")
    public String logout(HttpSession session, Model model) {
        BankAccount account = (BankAccount) session.getAttribute("account");

        model.addAttribute("account", null);
        logger.info("Logout from {}", account.getAccountNumber());
        return "redirect:/login";
    }

    @PostMapping("/deposit")
    public String deposit(@RequestParam float amount, HttpSession session) {
        BankAccount account = (BankAccount) session.getAttribute("account");
        if (account == null) return "redirect:/login";

        service.deposit(account.getAccountNumber(), amount);
        logger.info("Deposited {} from {}", amount, account.getAccountNumber());
        session.setAttribute("account", service.getById(account.getAccountNumber()));
        return "redirect:/";
    }

    @PostMapping("/withdraw")
    public String withdraw(@RequestParam float amount, HttpSession session) {
        BankAccount account = service.getById((Integer) session.getAttribute("accountID"));
        if (account == null) return "redirect:/login";

        service.withdraw(account.getAccountNumber(), amount);
        logger.info("Withdrawn {} from {}", amount, account.getAccountNumber());
        session.setAttribute("account", service.getById(account.getAccountNumber()));
        return "redirect:/";
    }
    @PostMapping("/transfer")
    public String transfer(@RequestParam float amount, @RequestParam int idTo, HttpSession session) {
        BankAccount account = service.getById((Integer) session.getAttribute("accountID"));
        if (account == null) return "redirect:/login";

        service.transfer(account.getAccountNumber(), idTo, amount);
        logger.info("Transferred {} from {} to {}", amount, account.getAccountNumber(),  idTo);
        session.setAttribute("account", service.getById(account.getAccountNumber()));
        return "redirect:/";
    }

    @PostMapping
    public BankAccount create(@RequestBody AccountRequest request) {
        return service.createAccount(request.getPassword(), request.getBalance());
    }
}

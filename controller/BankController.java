package com.BankingApp.controller;

import com.BankingApp.dto.LoginRequest;
import com.BankingApp.model.BankAccount;
import com.BankingApp.model.User;
import com.BankingApp.service.BankService;
import com.BankingApp.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@Controller
public class BankController {

    private final BankService bankService;
    private final UserService userService;
    final Logger logger = LogManager.getLogger(BankController.class);

    public BankController(BankService service, UserService userService) {
        this.bankService = service;
        this.userService = userService;
    }

    @GetMapping("/")
    public String showMenu(HttpSession session, Model model, Principal principal) {
        String email = principal.getName(); // from Spring Security

        // Load the user directly by email
        User user = userService.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("user", user);

        // Fetch all user's bank accounts
        List<BankAccount> accounts = bankService.getAccountsByUser(user.getId());
        model.addAttribute("accounts", accounts);

        // Handle selected account
        BankAccount selectedAccount = (BankAccount) session.getAttribute("selectedAccount");
        if (selectedAccount == null && !accounts.isEmpty()) {
            // If there is no selected account, then choose the first one by default
            selectedAccount = accounts.getFirst();
            session.setAttribute("selectedAccount", selectedAccount);
        }

        session.setAttribute("user", user);
        session.setAttribute("userId", user.getId());
        model.addAttribute("selectedAccount", selectedAccount);

        return "menu";
    }


    @PostMapping("/selectAccount")
    public String selectAccount(@RequestParam Long accountId, HttpSession session) {
        BankAccount account = bankService.getById(Math.toIntExact(accountId));

        // Verify this account belongs to the logged-in user for security
        if(account != null && account.getUser().getId() == (Integer) session.getAttribute("userId")){
            session.setAttribute("selectedAccount", account);
        }
        return "redirect:/";
    }

    @PostMapping("/deposit")
    public String deposit(@RequestParam float amount, HttpSession session, RedirectAttributes redirectAttrs) {
        BankAccount selectedAccount = (BankAccount) session.getAttribute("selectedAccount");
        if (selectedAccount == null) {
            redirectAttrs.addFlashAttribute("error", "Please select a bank account first.");
            return "redirect:/";
        }

        bankService.deposit(selectedAccount.getId(), amount);
        logger.info("Deposited {} from {}", amount, selectedAccount.getId());
        // Update the object for the view to show updated data
        session.setAttribute("selectedAccount", bankService.getById(selectedAccount.getId()));
        return "redirect:/";
    }

    @PostMapping("/withdraw")
    public String withdraw(@RequestParam float amount, HttpSession session, RedirectAttributes redirectAttrs) {
        BankAccount selectedAccount = (BankAccount) session.getAttribute("selectedAccount");
        if (selectedAccount == null) {
            redirectAttrs.addFlashAttribute("error", "Please select a bank account first.");
            return "redirect:/";
        }

        bankService.withdraw(selectedAccount.getId(), amount);
        logger.info("Withdrawn {} from {}", amount, selectedAccount.getId());

        // Update the object for the view to show updated data
        session.setAttribute("selectedAccount", bankService.getById(selectedAccount.getId()));
        return "redirect:/";
    }
    @PostMapping("/transfer")
    public String transfer(@RequestParam float amount, @RequestParam int idTo, HttpSession session, RedirectAttributes redirectAttrs) {
        BankAccount selectedAccount = (BankAccount) session.getAttribute("selectedAccount");
        if (selectedAccount == null) {
            redirectAttrs.addFlashAttribute("error", "Please select a bank account first.");
            return "redirect:/";
        }
        System.out.println("transfer from " + selectedAccount.getId() + " to " + idTo);
        bankService.transfer(selectedAccount.getId(), idTo, amount);
        logger.info("Transferred {} from {} to {}", amount, selectedAccount.getId(),  idTo);
        session.setAttribute("selectedAccount", bankService.getById(selectedAccount.getId()));
        return "redirect:/";
    }

    @GetMapping("/create")
    public String createAccount() {
        return  "createAccount";
    }

    @PostMapping("/create")
    public String create(@RequestParam float balance, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        bankService.createAccount(Math.toIntExact(userId), balance);
        return "redirect:/";  // back to menu after creation
    }

}

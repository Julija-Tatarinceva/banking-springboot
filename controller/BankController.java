package com.BankingApp.controller;

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
    private static final Logger logger = LogManager.getLogger(BankController.class);

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
    public String selectAccount(@RequestParam Long accountId, HttpSession session, RedirectAttributes redirectAttrs) {
        try {
            BankAccount account = bankService.getById(Math.toIntExact(accountId));
            Integer userId = (Integer) session.getAttribute("userId");
            // Verify this account belongs to the logged-in user for security
            if (account != null && account.getUser().getId() == userId) {
                session.setAttribute("selectedAccount", account);
            } else {
                redirectAttrs.addFlashAttribute("error", "You do not own this account.");
            }
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "Account selection failed: " + e.getMessage());
        }
        return "redirect:/";
    }

    @PostMapping("/deposit")
    public String deposit(@RequestParam BigDecimal amount, HttpSession session, RedirectAttributes redirectAttrs) {
        BankAccount selectedAccount = (BankAccount) session.getAttribute("selectedAccount");
        if (selectedAccount == null) {
            redirectAttrs.addFlashAttribute("error", "Please select a bank account first.");
            return "redirect:/";
        }


        try {
            bankService.deposit(selectedAccount.getId(), amount);
            logger.info("Deposited {} to account {}", amount, selectedAccount.getId());
            session.setAttribute("selectedAccount", bankService.getById(selectedAccount.getId()));
            redirectAttrs.addFlashAttribute("success", "Deposit successful.");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("depositError", "Deposit failed: " + e.getMessage());
        }        return "redirect:/";
    }

    @PostMapping("/withdraw")
    public String withdraw(@RequestParam BigDecimal amount, HttpSession session, RedirectAttributes redirectAttrs) {
        BankAccount selectedAccount = (BankAccount) session.getAttribute("selectedAccount");
        if (selectedAccount == null) {
            redirectAttrs.addFlashAttribute("error", "Please select a bank account first.");
            return "redirect:/";
        }

        try {
            bankService.withdraw(selectedAccount.getId(), amount);
            logger.info("Withdrawn {} from account {}", amount, selectedAccount.getId());
            session.setAttribute("selectedAccount", bankService.getById(selectedAccount.getId()));
            redirectAttrs.addFlashAttribute("success", "Withdrawal successful.");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("withdrawError", "Withdrawal failed: " + e.getMessage());
        }

        return "redirect:/";
    }
    @PostMapping("/transfer")
    public String transfer(@RequestParam BigDecimal amount, @RequestParam int idTo,
                           HttpSession session, RedirectAttributes redirectAttrs) {
        BankAccount selectedAccount = (BankAccount) session.getAttribute("selectedAccount");
        if (selectedAccount == null) {
            redirectAttrs.addFlashAttribute("error", "Please select a bank account first.");
            return "redirect:/";
        }

        try {
            logger.info("Attempting to transfer {} from {} to {}", amount, selectedAccount.getId(), idTo);
            bankService.transfer(selectedAccount.getId(), idTo, amount);
            session.setAttribute("selectedAccount", bankService.getById(selectedAccount.getId()));
            redirectAttrs.addFlashAttribute("success", "Transfer successful.");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("transferError", "Transfer failed: " + e.getMessage());
        }
        return "redirect:/";
    }

    @GetMapping("/create")
    public String createAccount() {
        return  "createAccount";
    }

    @PostMapping("/create")
    public String create(@RequestParam BigDecimal balance, HttpSession session,
                         RedirectAttributes redirectAttrs) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        try {
            bankService.createAccount(userId, balance);
            redirectAttrs.addFlashAttribute("success", "Bank account created.");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "Account creation failed: " + e.getMessage());
            return "redirect:/create"; // staying on creation page if didn't succeed
        }
        return "redirect:/";  // back to menu after creation
    }

}

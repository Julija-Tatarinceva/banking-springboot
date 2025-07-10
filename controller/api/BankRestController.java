package com.BankingApp.controller.api;

import com.BankingApp.model.BankAccount;
import com.BankingApp.model.User;
import com.BankingApp.service.BankService;
import com.BankingApp.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class BankRestController {

    private final BankService bankService;
    private final UserService userService;
    private static final Logger logger = LogManager.getLogger(BankRestController.class);

    public BankRestController(BankService bankService, UserService userService) {
        this.bankService = bankService;
        this.userService = userService;
    }

    @GetMapping("/menu")
    public Map<String, Object> showMenu(HttpSession session, Principal principal) {
        String email = principal.getName();
        User user = userService.findUserByEmail(email);

        List<BankAccount> accounts = bankService.getAccountsByUser(user.getId());
        BankAccount selectedAccount = (BankAccount) session.getAttribute("selectedAccount");

        if (selectedAccount == null && !accounts.isEmpty()) {
            selectedAccount = accounts.getFirst();
            session.setAttribute("selectedAccount", selectedAccount);
        }

        session.setAttribute("user", user);
        session.setAttribute("userId", user.getId());

        Map<String, Object> response = new HashMap<>();
        response.put("user", user);
        response.put("accounts", accounts);
        response.put("selectedAccount", selectedAccount);
        return response;
    }

    @PostMapping("/selectAccount")
    public Map<String, String> selectAccount(@RequestParam Long accountId, HttpSession session) {
        Map<String, String> result = new HashMap<>();
        try {
            BankAccount account = bankService.getById(Math.toIntExact(accountId));
            Integer userId = (Integer) session.getAttribute("userId");
            if (account != null && account.getUser().getId() == userId) {
                session.setAttribute("selectedAccount", account);
                result.put("message", "Account selected");
            } else {
                result.put("error", "You do not own this account.");
            }
        } catch (Exception e) {
            result.put("error", "Account selection failed: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/deposit")
    public Map<String, String> deposit(@RequestParam BigDecimal amount, HttpSession session) {
        Map<String, String> result = new HashMap<>();
        BankAccount selectedAccount = (BankAccount) session.getAttribute("selectedAccount");

        if (selectedAccount == null) {
            result.put("error", "Please select a bank account first.");
            return result;
        }

        try {
            bankService.deposit(selectedAccount.getId(), amount);
            logger.info("Deposited {} to account {}", amount, selectedAccount.getId());
            session.setAttribute("selectedAccount", bankService.getById(selectedAccount.getId()));
            result.put("message", "Deposit successful");
        } catch (Exception e) {
            result.put("error", "Deposit failed: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/withdraw")
    public Map<String, String> withdraw(@RequestParam BigDecimal amount, HttpSession session) {
        Map<String, String> result = new HashMap<>();
        BankAccount selectedAccount = (BankAccount) session.getAttribute("selectedAccount");

        if (selectedAccount == null) {
            result.put("error", "Please select a bank account first.");
            return result;
        }

        try {
            bankService.withdraw(selectedAccount.getId(), amount);
            logger.info("Withdrawn {} from account {}", amount, selectedAccount.getId());
            session.setAttribute("selectedAccount", bankService.getById(selectedAccount.getId()));
            result.put("message", "Withdrawal successful");
        } catch (Exception e) {
            result.put("error", "Withdrawal failed: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/transfer")
    public Map<String, String> transfer(@RequestParam BigDecimal amount, @RequestParam int idTo,
                                        HttpSession session) {
        Map<String, String> result = new HashMap<>();
        BankAccount selectedAccount = (BankAccount) session.getAttribute("selectedAccount");

        if (selectedAccount == null) {
            result.put("error", "Please select a bank account first.");
            return result;
        }

        try {
            logger.info("Attempting to transfer {} from {} to {}", amount, selectedAccount.getId(), idTo);
            bankService.transfer(selectedAccount.getId(), idTo, amount);
            session.setAttribute("selectedAccount", bankService.getById(selectedAccount.getId()));
            result.put("message", "Transfer successful");
        } catch (Exception e) {
            result.put("error", "Transfer failed: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/create")
    public Map<String, String> create(@RequestParam BigDecimal balance, HttpSession session) {
        Map<String, String> result = new HashMap<>();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            result.put("error", "Not logged in.");
            return result;
        }

        try {
            bankService.createAccount(userId, balance);
            result.put("message", "Bank account created.");
        } catch (Exception e) {
            result.put("error", "Account creation failed: " + e.getMessage());
        }

        return result;
    }
}

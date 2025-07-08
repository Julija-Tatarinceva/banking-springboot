package com.BankingApp.service;

import com.BankingApp.model.BankAccount;
import com.BankingApp.model.Transaction;
import com.BankingApp.model.User;
import com.BankingApp.repository.BankAccountRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankService {

//    public static Map<Integer, BankAccount> accounts = Storage.loadAccounts("C:\\Users\\juuli\\Downloads\\BankingApp\\BankingApp\\src\\main\\java\\com\\BankingApp\\storage\\Accounts.txt");
    private final BankAccountRepository bankAccountRepository;
    private final UserService userService;

    //    private static final Logger logger = LogManager.getLogger(BankService.class);
    //    public static BankAccount account =  new BankAccount(1234, "pass", 500.0f);
//    public static Map<Integer, BankAccount> accounts = Map.of(
//            1234, account
//    );
    @Autowired
    public BankService(BankAccountRepository repository, UserService userService) {
        this.bankAccountRepository = repository;
        this.userService = userService;
    }

    public BankAccount getById(int id) {
        return bankAccountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    public BankAccount createAccount(int userId, float balance) {
        User userToBind = userService.findUserById(userId);
        BankAccount account = new BankAccount(userToBind, balance);
        return bankAccountRepository.save(account);
    }

    public Transaction transfer(int fromId, int toId, float amount) {
        BankAccount accountFrom = bankAccountRepository.findById(fromId)
                .orElseThrow(() -> new RuntimeException("Account from not found"));
        BankAccount accountTo = bankAccountRepository.findById(toId)
                .orElseThrow(() -> new RuntimeException("Account to not found"));

        final Logger logger = LogManager.getLogger(BankService.class);

        System.out.println(accountTo);
        Transaction newTransaction = accountFrom.transferTo(accountTo, amount);

        bankAccountRepository.save(accountFrom);
        bankAccountRepository.save(accountTo);
        return newTransaction;
    }
    public Transaction deposit(int toId, float amount) {
        final Logger logger = LogManager.getLogger(BankService.class);

        BankAccount accountTo = bankAccountRepository.findById(toId)
                .orElseThrow(() -> new RuntimeException("Account from not found"));
        System.out.println("Deposited " + amount + " from " + accountTo.getId());
        logger.info("Info log message BANK");
        logger.error("Error log message BANK");
        Transaction newTransaction = accountTo.deposit(amount);
        bankAccountRepository.save(accountTo);
        return newTransaction;
    }
    public Transaction withdraw(int fromId, float amount) {
        final Logger logger = LogManager.getLogger(BankService.class);

        BankAccount accountFrom = bankAccountRepository.findById(fromId)
                .orElseThrow(() -> new RuntimeException("Account from not found"));
        Transaction newTransaction = accountFrom.withdraw(amount);
        bankAccountRepository.save(accountFrom);
        return newTransaction;
    }

    public List<BankAccount> getAccountsByUser(Integer userId) {
        return bankAccountRepository.findByUserId(userId);
    }

//    public boolean authenticate(int id, String password) {
//        final Logger logger = LogManager.getLogger(BankService.class);
//        BankAccount acc = bankAccountRepository.findById(id);
//        logger.info("Attempted login into account {}", id);
//        return acc.authenticate(password);
//    }
}

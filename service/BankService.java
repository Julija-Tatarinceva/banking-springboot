package com.BankingApp.service;

import com.BankingApp.BankingApplication;
import com.BankingApp.model.BankAccount;
import com.BankingApp.model.Transaction;
import com.BankingApp.repository.BankAccountRepository;
import com.BankingApp.storage.Storage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class BankService {

    public static Map<Integer, BankAccount> accounts = Storage.loadAccounts("C:\\Users\\juuli\\Downloads\\BankingApp\\BankingApp\\src\\main\\java\\com\\BankingApp\\storage\\Accounts.txt");
    private final BankAccountRepository bankAccountRepository;
    //    private static final Logger logger = LogManager.getLogger(BankService.class);
    //    public static BankAccount account =  new BankAccount(1234, "pass", 500.0f);
//    public static Map<Integer, BankAccount> accounts = Map.of(
//            1234, account
//    );
    @Autowired
    public BankService(BankAccountRepository repository) {
        this.bankAccountRepository = repository;
    }

    public BankAccount getById(int id) {
        return bankAccountRepository.findById(id);
    }

    public BankAccount createAccount(String password, float balance) {
        BankAccount account = new BankAccount(password, balance);
        System.out.println(balance);
        return bankAccountRepository.save(account);
    }


    public Transaction transfer(int fromId, int toId, float amount) {
        BankAccount from = bankAccountRepository.findById(fromId);;
        BankAccount to = bankAccountRepository.findById(toId);

        final Logger logger = LogManager.getLogger(BankService.class);

        System.out.println(to);
        Transaction newTransaction = from.transferTo(to, amount);

        bankAccountRepository.updateAccount(from);
        bankAccountRepository.updateAccount(to);
        return newTransaction;
    }
    public Transaction deposit(int toId, float amount) {
        final Logger logger = LogManager.getLogger(BankService.class);

        BankAccount to = bankAccountRepository.findById(toId);
        System.out.println("Deposited " + amount + " from " + to.getAccountNumber());
        logger.info("Info log message BANK");
        logger.error("Error log message BANK");
        Transaction newTransaction = to.deposit(amount);
        bankAccountRepository.updateAccount(to);
        return newTransaction;
    }
    public Transaction withdraw(int fromId, float amount) {
        final Logger logger = LogManager.getLogger(BankService.class);

        BankAccount from = bankAccountRepository.findById(fromId);
        Transaction newTransaction = from.withdraw(amount);
        bankAccountRepository.updateAccount(from);
        return newTransaction;
    }

    public boolean authenticate(int id, String password) {
        final Logger logger = LogManager.getLogger(BankService.class);
        BankAccount acc = bankAccountRepository.findById(id);
        logger.info("Attempted login into account {}", id);
        return acc.authenticate(password);
    }
}

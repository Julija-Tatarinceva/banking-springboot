package com.BankingApp.service;

import com.BankingApp.exception.BankAccountNotFoundException;
import com.BankingApp.exception.InsufficientFundsException;
import com.BankingApp.exception.InvalidTransactionAmountException;
import com.BankingApp.exception.TransferToSelfException;
import com.BankingApp.model.BankAccount;
import com.BankingApp.model.Transaction;
import com.BankingApp.model.User;
import com.BankingApp.repository.BankAccountRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BankService {
    private static final Logger logger = LogManager.getLogger(BankService.class);
    private final BankAccountRepository bankAccountRepository;
    private final UserService userService;

    @Autowired
    public BankService(BankAccountRepository repository, UserService userService) {
        this.bankAccountRepository = repository;
        this.userService = userService;
    }

    public BankAccount getById(int id) {
        return bankAccountRepository.findById(id)
                .orElseThrow(() -> new BankAccountNotFoundException("Account not found"));
    }

    public BankAccount createAccount(int userId, BigDecimal balance) {
        User userToBind = userService.findUserById(userId);
        if (balance == null || balance.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransactionAmountException("Attempted to create account with non-positive balance");
        }
        BankAccount account = new BankAccount(userToBind, balance);
        return bankAccountRepository.save(account);
    }

    public Transaction transfer(int fromId, int toId, BigDecimal amount) {

        // Validation
        BankAccount accountFrom = bankAccountRepository.findById(fromId)
                .orElseThrow(() -> new BankAccountNotFoundException("Transfer: transfer sender not found"));
        BankAccount accountTo = bankAccountRepository.findById(toId)
                .orElseThrow(() -> new BankAccountNotFoundException("Transfer: transfer receiver not found"));
        if (accountFrom == accountTo) {
            throw new TransferToSelfException("Attempted to transfer to self.");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransactionAmountException("Attempted to transfer non-positive amount");
        }

        Transaction newTransaction = accountFrom.transferTo(accountTo, amount);
        logger.info(newTransaction);
        bankAccountRepository.save(accountFrom);
        bankAccountRepository.save(accountTo);
        return newTransaction;
    }

    public Transaction deposit(int toId, BigDecimal amount) {

        // Validation
        if(amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransactionAmountException("Attempted to deposit non-positive amount");
        }
        BankAccount accountTo = bankAccountRepository.findById(toId)
                .orElseThrow(() -> new BankAccountNotFoundException("Deposit: account 'to' not found"));
        System.out.println("Deposited " + amount + " from " + accountTo.getId());

        Transaction newTransaction = accountTo.deposit(amount);
        bankAccountRepository.save(accountTo);
        return newTransaction;
    }

    public Transaction withdraw(int fromId, BigDecimal amount) {

        // Validation
        if(amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransactionAmountException("Attempted to withdraw non-positive amount");
        }
        BankAccount accountFrom = bankAccountRepository.findById(fromId)
                .orElseThrow(() -> new BankAccountNotFoundException("Withdraw: account 'from' not found"));
        if (accountFrom.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Not enough balance.");
        }

        Transaction newTransaction = accountFrom.withdraw(amount);
        logger.info(newTransaction);
        bankAccountRepository.save(accountFrom);
        return newTransaction;
    }

    public List<BankAccount> getAccountsByUser(Integer userId) {
        return bankAccountRepository.findByUserId(userId);
    }
}

package com.BankingApp.repository;

import com.BankingApp.model.BankAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BankAccountRepository {

    private final JdbcTemplate jdbc;

    @Autowired
    public BankAccountRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public BankAccount findById(int id) {
        return jdbc.queryForObject(
                "SELECT * FROM accounts WHERE id = ?",
                new Object[]{id},
                (rs, rowNum) -> new BankAccount(
                        rs.getInt("id"),
                        rs.getString("password"),
                        rs.getFloat("balance"))
        );
    }

    public BankAccount save(BankAccount account) {
        jdbc.update(
                "INSERT INTO accounts (id, password, balance) VALUES (?, ?, ?)",
                account.getAccountNumber(), account.getPassword(), account.getBalance()
        );
        return account;
    }

    public int updateAccount(BankAccount account) {
        String sql = "UPDATE accounts SET password = ?, balance = ? WHERE id = ?";
        return jdbc.update(sql, account.getPassword(), account.getBalance(), account.getAccountNumber());
    }

}

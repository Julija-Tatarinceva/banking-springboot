package com.BankingApp.repository;

import com.BankingApp.model.BankAccount;
import com.BankingApp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface  BankAccountRepository extends JpaRepository<BankAccount, Integer> {
    Optional<BankAccount> findById(int id);
//    List<BankAccount> findByUser(User user);

    List<BankAccount> findByUserId(Integer userId);

//    private final JdbcTemplate jdbc;
//
//    @Autowired
//    public BankAccountRepository(JdbcTemplate jdbc) {
//        this.jdbc = jdbc;
//    }
//
//    public BankAccount findById(int id) {
//        return jdbc.queryForObject(
//                "SELECT * FROM accounts WHERE id = ?",
//                new Object[]{id},
//                (rs, rowNum) -> new BankAccount(
//                        rs.getInt("id"),
//                        rs.getString("password"),
//                        rs.getFloat("balance"))
//        );
//    }
//
//    public BankAccount save(BankAccount account) {
//        jdbc.update(
//                "INSERT INTO accounts (id, balance) VALUES (?, ?)",
//                account.getAccountNumber(), account.getBalance()
//        );
//        return account;
//    }
//
//    public int updateAccount(BankAccount account) {
//        String sql = "UPDATE accounts SET balance = ? WHERE id = ?";
//        return jdbc.update(sql, account.getBalance(), account.getAccountNumber());
//    }
}

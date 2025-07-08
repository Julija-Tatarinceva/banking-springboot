package com.BankingApp.repository;

import com.BankingApp.model.BankAccount;
import com.BankingApp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    //    private final JdbcTemplate jdbc;
//
//    @Autowired
//    public UserRepository(JdbcTemplate jdbc) {
//        this.jdbc = jdbc;
//    }
//
//    public User findById(int id) {
//        return jdbc.queryForObject(
//                "SELECT * FROM users WHERE id = ?",
//                new Object[]{id},
//                (rs, rowNum) -> new User(
//                        rs.getInt("id"),
//                        rs.getString("email"),
//                        rs.getString("password"),
//                        rs.getString("name")
//                )
//        );
//    }
//
//    public User findByEmail(String email) {
//        return jdbc.queryForObject(
//                "SELECT * FROM users WHERE email = ?",
//                new Object[]{email},
//                (rs, rowNum) -> new User(
//                        rs.getInt("id"),
//                        rs.getString("email"),
//                        rs.getString("password"),
//                        rs.getString("name")
//                )
//        );
//    }
//
//    public User save(User user) {
//        jdbc.update(
//                "INSERT INTO users (email, password, name) VALUES (?, ?, ?)",
//                user.getEmail(), user.getPassword(), user.getName()
//        );
//        // You may want to return the created user with ID if you're using auto-increment:
//        return user;
//    }
//
//    public int updateUser(User user) {
//        String sql = "UPDATE users SET email = ?, password = ?, name = ? WHERE id = ?";
//        return jdbc.update(sql, user.getEmail(), user.getPassword(), user.getName(), user.getId());
//    }

}

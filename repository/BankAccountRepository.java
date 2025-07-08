package com.BankingApp.repository;

import com.BankingApp.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface  BankAccountRepository extends JpaRepository<BankAccount, Integer> {
    Optional<BankAccount> findById(int id);

    List<BankAccount> findByUserId(Integer userId);
}

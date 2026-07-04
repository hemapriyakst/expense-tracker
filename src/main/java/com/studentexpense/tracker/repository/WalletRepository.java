package com.studentexpense.tracker.repository;

import com.studentexpense.tracker.entity.Wallet;
import com.studentexpense.tracker.entity.enums.WalletType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * JpaRepository<Wallet, Long> already gives us save(), findById(), findAll(),
 * delete() etc. for free. We only declare the extra finder method we need —
 * Spring Data JPA generates the implementation from the method name itself.
 */
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Optional<Wallet> findByType(WalletType type);
}

package com.studentexpense.tracker.repository;

import com.studentexpense.tracker.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // For the "recent transactions" list - most recent first.
    // Spring Data JPA parses this method name into a query automatically:
    // "findAllByOrderByDateDescCreatedAtDesc" -> ORDER BY date DESC, createdAt DESC
    List<Transaction> findAllByOrderByDateDescCreatedAtDesc();

    // For the "daily spending, last 30 days" bar chart
    List<Transaction> findByDateBetween(LocalDate start, LocalDate end);

    // Custom JPQL query for category breakdown (donut chart) —
    // method-name-derived queries can't easily do "group by" style needs
    // at the entity level, so we filter and aggregate in the service layer
    // using this simpler filtered fetch.
    @Query("SELECT t FROM Transaction t WHERE t.type = 'EXPENSE' AND t.date BETWEEN :start AND :end")
    List<Transaction> findExpensesBetween(@Param("start") LocalDate start, @Param("end") LocalDate end);
}

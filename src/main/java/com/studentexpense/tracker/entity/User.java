package com.studentexpense.tracker.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Single-user app for now, but modeled as a real entity (not hardcoded
 * credentials) so multi-user support is a small extension later, not a rewrite.
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    // Stored as a BCrypt hash — NEVER plain text
    @Column(nullable = false)
    private String password;
}

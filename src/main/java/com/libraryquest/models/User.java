package com.libraryquest.models;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Класс, представляющий пользователя
 */
@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password; //TODO: Хранить зашифрованным

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

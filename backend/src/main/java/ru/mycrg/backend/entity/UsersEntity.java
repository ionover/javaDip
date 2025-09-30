package ru.mycrg.backend.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name = "users", schema = "data")
public class UsersEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "login", nullable = false)
    private String title;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "token")
    private String token;
}

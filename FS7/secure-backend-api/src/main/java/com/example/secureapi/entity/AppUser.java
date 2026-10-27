package com.example.secureapi.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "app_users", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
public class AppUser {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, unique = true) private String username;
    @Column(nullable = false) private String passwordHash;
    @Column(nullable = false) private String role;
    protected AppUser() {}
    public AppUser(String username, String passwordHash, String role) { this.username=username; this.passwordHash=passwordHash; this.role=role; }
    public Long getId(){return id;} public String getUsername(){return username;} public String getPasswordHash(){return passwordHash;} public String getRole(){return role;}
}

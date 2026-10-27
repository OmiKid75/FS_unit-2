package com.example.secureapi.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name="encrypted_credentials")
public class EncryptedCredential {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false) private String label;
    @Column(nullable=false, length=4000) private String ciphertext;
    @Column(nullable=false) private Instant createdAt;
    protected EncryptedCredential() {}
    public EncryptedCredential(String label,String ciphertext){this.label=label;this.ciphertext=ciphertext;this.createdAt=Instant.now();}
    public Long getId(){return id;} public String getLabel(){return label;} public String getCiphertext(){return ciphertext;} public Instant getCreatedAt(){return createdAt;}
}

package com.example.secureapi.repository;
import com.example.secureapi.entity.EncryptedCredential;
import org.springframework.data.jpa.repository.JpaRepository;
public interface EncryptedCredentialRepository extends JpaRepository<EncryptedCredential,Long>{}

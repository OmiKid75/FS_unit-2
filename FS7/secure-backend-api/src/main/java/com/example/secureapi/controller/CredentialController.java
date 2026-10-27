package com.example.secureapi.controller;

import com.example.secureapi.dto.CredentialRequest;
import com.example.secureapi.entity.EncryptedCredential;
import com.example.secureapi.repository.EncryptedCredentialRepository;
import com.example.secureapi.service.AesEncryptionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController @RequestMapping("/api/credentials") @PreAuthorize("hasRole('ADMIN')")
public class CredentialController {
    private final EncryptedCredentialRepository repository; private final AesEncryptionService aes;
    public CredentialController(EncryptedCredentialRepository repository,AesEncryptionService aes){this.repository=repository;this.aes=aes;}
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public Map<String,Object> store(@Valid @RequestBody CredentialRequest req){EncryptedCredential saved=repository.save(new EncryptedCredential(req.label(),aes.encrypt(req.secret())));return Map.of("id",saved.getId(),"label",saved.getLabel(),"ciphertext",saved.getCiphertext(),"createdAt",saved.getCreatedAt(),"note","Secret is stored encrypted; plaintext is not returned");}
    @GetMapping("/{id}") public Map<String,Object> get(@PathVariable Long id){EncryptedCredential c=repository.findById(id).orElseThrow(()->new org.springframework.web.server.ResponseStatusException(HttpStatus.NOT_FOUND,"Credential not found"));return Map.of("id",c.getId(),"label",c.getLabel(),"ciphertext",c.getCiphertext(),"createdAt",c.getCreatedAt());}
}

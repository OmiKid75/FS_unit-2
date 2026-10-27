package com.example.secureapi.controller;

import com.example.secureapi.dto.CryptoRequest;
import com.example.secureapi.service.AesEncryptionService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController @RequestMapping("/api/crypto")
public class CryptoController {
    private final AesEncryptionService aes; public CryptoController(AesEncryptionService aes){this.aes=aes;}
    @PostMapping("/encrypt") public Map<String,String> encrypt(@Valid @RequestBody CryptoRequest req){return Map.of("ciphertext",aes.encrypt(req.value()),"algorithm","AES-256-GCM","note","Demo endpoint; do not send real secrets");}
    @PostMapping("/decrypt") @PreAuthorize("hasRole('ADMIN')") public Map<String,String> decrypt(@Valid @RequestBody CryptoRequest req){return Map.of("plaintext",aes.decrypt(req.value()),"algorithm","AES-256-GCM");}
}

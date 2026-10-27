package com.example.secureapi.dto;
import jakarta.validation.constraints.NotBlank;
public record CredentialRequest(@NotBlank String label,@NotBlank String secret) {}

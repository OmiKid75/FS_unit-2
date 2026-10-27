package com.example.secureapi.dto;
import jakarta.validation.constraints.NotBlank;
public record CryptoRequest(@NotBlank String value) {}

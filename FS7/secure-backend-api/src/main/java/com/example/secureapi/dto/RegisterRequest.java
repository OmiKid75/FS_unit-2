package com.example.secureapi.dto;
import jakarta.validation.constraints.NotBlank;
public record RegisterRequest(@NotBlank String username,@NotBlank String password) {}

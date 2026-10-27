package com.example.secureapi.dto;
public record TokenResponse(String tokenType,String accessToken,String refreshToken,long accessTokenExpiresInSeconds,String username,String role) {}

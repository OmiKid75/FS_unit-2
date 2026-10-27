package com.example.secureapi.service;

import com.example.secureapi.dto.*;
import com.example.secureapi.entity.AppUser;
import com.example.secureapi.entity.RefreshToken;
import com.example.secureapi.repository.AppUserRepository;
import com.example.secureapi.repository.RefreshTokenRepository;
import com.example.secureapi.security.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;

@Service
public class AuthService {
    private final AppUserRepository users; private final RefreshTokenRepository refreshTokens; private final PasswordEncoder encoder; private final JwtService jwt; private final long refreshDays; private final SecureRandom random=new SecureRandom();
    public AuthService(AppUserRepository users,RefreshTokenRepository refreshTokens,PasswordEncoder encoder,JwtService jwt,@Value("${app.jwt.refresh-token-days}") long refreshDays){this.users=users;this.refreshTokens=refreshTokens;this.encoder=encoder;this.jwt=jwt;this.refreshDays=refreshDays;}
    public AppUser register(RegisterRequest req){if(users.existsByUsername(req.username()))throw new ResponseStatusException(HttpStatus.CONFLICT,"Username already exists");return users.save(new AppUser(req.username(),encoder.encode(req.password()),"USER"));}
    public TokenResponse login(LoginRequest req){AppUser user=users.findByUsername(req.username()).orElseThrow(()->new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid username or password"));if(!encoder.matches(req.password(),user.getPasswordHash()))throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid username or password");return issue(user);}
    @Transactional public TokenResponse refresh(String raw){String hash=hash(raw);RefreshToken old=refreshTokens.findByTokenHash(hash).orElseThrow(()->new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid refresh token"));if(old.isRevoked()||old.getExpiresAt().isBefore(Instant.now()))throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Refresh token expired or revoked");old.revoke();AppUser user=users.findByUsername(old.getUsername()).orElseThrow(()->new ResponseStatusException(HttpStatus.UNAUTHORIZED,"User not found"));return issue(user);}
    @Transactional public void logout(String raw){refreshTokens.findByTokenHash(hash(raw)).ifPresent(RefreshToken::revoke);}
    private TokenResponse issue(AppUser user){String access=jwt.createAccessToken(user.getUsername(),user.getRole());byte[] bytes=new byte[48];random.nextBytes(bytes);String refresh=Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);refreshTokens.save(new RefreshToken(hash(refresh),user.getUsername(),Instant.now().plusSeconds(refreshDays*86400)));return new TokenResponse("Bearer",access,refresh,jwt.getAccessTokenSeconds(),user.getUsername(),user.getRole());}
    private String hash(String token){try{return java.util.HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(token.getBytes(StandardCharsets.UTF_8)));}catch(Exception e){throw new IllegalStateException(e);}}
}

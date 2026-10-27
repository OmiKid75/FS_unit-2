package com.example.secureapi.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {
    private final SecretKey key; private final long accessMinutes;
    public JwtService(@Value("${app.jwt.secret-base64}") String secret,@Value("${app.jwt.access-token-minutes}") long accessMinutes){
        byte[] bytes=Decoders.BASE64.decode(secret); if(bytes.length<32) throw new IllegalArgumentException("JWT secret must decode to at least 32 bytes");
        this.key=Keys.hmacShaKeyFor(bytes); this.accessMinutes=accessMinutes;
    }
    public String createAccessToken(String username,String role){Instant now=Instant.now();return Jwts.builder().subject(username).claim("role",role).issuedAt(Date.from(now)).expiration(Date.from(now.plusSeconds(accessMinutes*60))).signWith(key).compact();}
    public Claims parse(String token){return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();}
    public boolean isValid(String token){try{return parse(token).getExpiration().after(new Date());}catch(Exception ex){return false;}}
    public long getAccessTokenSeconds(){return accessMinutes*60;}
}

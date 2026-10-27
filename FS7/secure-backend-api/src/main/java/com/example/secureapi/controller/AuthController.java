package com.example.secureapi.controller;

import com.example.secureapi.dto.*;
import com.example.secureapi.entity.AppUser;
import com.example.secureapi.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController @RequestMapping("/api/auth")
public class AuthController {
    private final AuthService auth; public AuthController(AuthService auth){this.auth=auth;}
    @PostMapping("/register") @ResponseStatus(HttpStatus.CREATED) public Map<String,Object> register(@Valid @RequestBody RegisterRequest req){AppUser user=auth.register(req);return Map.of("message","User registered","username",user.getUsername(),"role",user.getRole());}
    @PostMapping("/login") public TokenResponse login(@Valid @RequestBody LoginRequest req){return auth.login(req);}
    @PostMapping("/refresh") public TokenResponse refresh(@Valid @RequestBody RefreshRequest req){return auth.refresh(req.refreshToken());}
    @PostMapping("/logout") public Map<String,String> logout(@Valid @RequestBody RefreshRequest req){auth.logout(req.refreshToken());return Map.of("message","Refresh token revoked if it existed");}
    @GetMapping("/me") public Map<String,String> me(Authentication authn){return Map.of("username",authn.getName(),"authorities",authn.getAuthorities().toString());}
}

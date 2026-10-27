package com.example.secureapi.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController @RequestMapping("/api")
public class DemoController {
    @GetMapping("/user/profile") @PreAuthorize("hasAnyRole('USER','ADMIN')") public Map<String,String> profile(Authentication auth){return Map.of("message","Authenticated profile access granted","username",auth.getName());}
    @GetMapping("/admin/dashboard") @PreAuthorize("hasRole('ADMIN')") public Map<String,String> admin(Authentication auth){return Map.of("message","Admin-only endpoint access granted","username",auth.getName());}
    @GetMapping("/public/hello") public Map<String,String> hello(){return Map.of("message","Hello! This endpoint is public.");}
}

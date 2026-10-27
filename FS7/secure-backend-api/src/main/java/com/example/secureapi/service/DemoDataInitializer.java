package com.example.secureapi.service;

import com.example.secureapi.entity.AppUser;
import com.example.secureapi.repository.AppUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DemoDataInitializer {
    @Bean CommandLineRunner seedUsers(AppUserRepository users,PasswordEncoder encoder){return args->{
        if(!users.existsByUsername("admin"))users.save(new AppUser("admin",encoder.encode("Admin123!"),"ADMIN"));
        if(!users.existsByUsername("student"))users.save(new AppUser("student",encoder.encode("Student123!"),"USER"));
    };}
}

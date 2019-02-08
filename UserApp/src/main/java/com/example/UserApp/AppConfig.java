package com.example.UserApp;

import com.example.UserApp.repo.SecurityRoleRepo;
import com.example.UserApp.repo.UserRepository;
import com.example.UserApp.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppConfig {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public UserService userService(UserRepository ur, SecurityRoleRepo srr) {
    return  new UserService(passwordEncoder(), ur, srr);
  }
}

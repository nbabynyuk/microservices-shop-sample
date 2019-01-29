package com.example.UserApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AuthProvider extends DaoAuthenticationProvider {

  @Autowired
  public AuthProvider (UserService userService, PasswordEncoder encoder){
    super();
    this.setUserDetailsService(userService);
    this.setPasswordEncoder(encoder);
  }
}

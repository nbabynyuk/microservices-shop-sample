package com.example.UserApp.web;

import com.example.UserApp.dto.LoginRequest;
import com.example.UserApp.service.AuthProvider;
import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

  private final AuthProvider authProvider;

  @Autowired
  public LoginController(AuthProvider authProvider) {
    this.authProvider = authProvider;
  }

  @GetMapping(value = "/", produces = "application/json")
  public Map<String, String> index() {
    Map<String, String> m = new HashMap<>();
    m.put("name", "UserApp");
    m.put("version", "1.0");
    return m;
  }

  @PostMapping(value = "/api/login", produces = "application/json")
  public ResponseEntity<?> login(@RequestBody @Valid LoginRequest loginRequest,
      BindingResult bindingResult) {

    if (bindingResult.hasErrors() || (loginRequest == null)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    } else {
      UsernamePasswordAuthenticationToken credentials = new UsernamePasswordAuthenticationToken(
          loginRequest.getUsername(),
          loginRequest.getPassword());
      authProvider.authenticate(credentials);
      return ResponseEntity.ok().build();
    }
  }
}

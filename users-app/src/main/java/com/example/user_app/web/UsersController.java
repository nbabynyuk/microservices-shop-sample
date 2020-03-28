package com.example.user_app.web;

import com.example.user_app.dto.UserRegistrationRequest;
import com.example.user_app.entity.UserEntity;
import com.example.user_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(value = "/api/users")
public class UsersController {

  private final UserService userService;

  @Autowired
  public UsersController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> createUser(@RequestBody @Valid UserRegistrationRequest urr) {
    UserEntity u = userService.createUser(urr);
    URI location = URI.create("/api/users/" + u.getId());
    return ResponseEntity.created(location).build();
  }
}
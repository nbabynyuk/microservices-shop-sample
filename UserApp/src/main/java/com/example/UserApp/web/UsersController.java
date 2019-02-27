package com.example.UserApp.web;

import static com.example.UserApp.AppConstants.USER_INPUT_ERROR;

import com.example.UserApp.dto.UserRegistrationRequest;
import com.example.UserApp.entity.UserEntity;
import com.example.UserApp.service.UserService;
import java.net.URI;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UsersController {

  private final UserService userService;

  @Autowired
  public UsersController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping(value = "/api/users/registration", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> createUser(@RequestBody @Valid UserRegistrationRequest urr) {
    UserEntity u = userService.createUser(urr);
    URI location = URI.create("/api/users/" + u.getId());
    return ResponseEntity.created(location).build();
  }

}
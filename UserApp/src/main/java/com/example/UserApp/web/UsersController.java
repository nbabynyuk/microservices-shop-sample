package com.example.UserApp.web;

import com.example.UserApp.AppConstants;
import com.example.UserApp.dto.UserRegistrationRequest;
import com.example.UserApp.entity.UserEntity;
import com.example.UserApp.errors.ErrorResponse;
import com.example.UserApp.errors.PropertyValueError;
import com.example.UserApp.service.UserService;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
  public ResponseEntity<?> createUser(@RequestBody @Valid UserRegistrationRequest urr,
      BindingResult bindingResult) {
    if (bindingResult.hasErrors() || (urr == null)) {
      HttpHeaders headers = new HttpHeaders();
      List<PropertyValueError> fiedlErrors = bindingResult.getAllErrors().stream().map( oe ->
          new PropertyValueError(
              oe.getObjectName(),
              oe.getCode(),
              oe.getDefaultMessage()))
          .collect(Collectors.toList());
      ErrorResponse errorResponse = new ErrorResponse(AppConstants.USER_INPUT_ERROR,
          "",
          fiedlErrors);
      headers.add("errors", errorResponse.toJSON());
      return  new ResponseEntity<>(urr, headers, HttpStatus.BAD_REQUEST);
    } else {
      UserEntity u = userService.createUser(urr);
      URI location =  URI.create("/api/users/" + u.getId());
      return ResponseEntity.created(location).build();
    }
  }

}

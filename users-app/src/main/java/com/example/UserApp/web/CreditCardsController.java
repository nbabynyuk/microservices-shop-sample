package com.example.UserApp.web;

import com.example.UserApp.dto.PaymentMethodUpdateRequests;
import com.example.UserApp.errors.UserNotFoundException;
import com.example.UserApp.service.UserService;
import com.nb.common.CreditCardDTO;
import io.micrometer.core.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class CreditCardsController {

  private final UserService userService;

  @Autowired
  public CreditCardsController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping(value = "/api/users/{userId}/creditCards", produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<List<CreditCardDTO>> getPaymentInfo(@PathVariable Long userId) {
    return userService.getCreditCards(userId).map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PutMapping(value = "/api/users/{userId}/creditCards", produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<?> updatePaymentInfo(@PathVariable Long userId,
      @Valid @RequestBody PaymentMethodUpdateRequests updateRequest) {
      try {
        userService.updatePaymentMethod(userId, updateRequest);
        return ResponseEntity.ok().build();
      } catch (UserNotFoundException une) {
        return ResponseEntity.notFound().build();
      }
  }
}

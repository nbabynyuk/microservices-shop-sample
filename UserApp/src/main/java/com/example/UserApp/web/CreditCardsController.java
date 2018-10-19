package com.example.UserApp.web;

import static com.example.UserApp.AppConstants.CREDIT_CARD_INPUT_ERROR;

import com.example.UserApp.dto.PaymentMethodUpdateRequests;
import com.example.UserApp.errors.UserNotFoundException;
import com.example.UserApp.service.UserService;
import com.nb.common.CreditCardDTO;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CreditCardsController implements DefaultErroHandler {

  private final UserService userService;

  @Autowired
  public CreditCardsController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping(value = "/api/users/{userId}/creditCards", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<CreditCardDTO>> getPaymentInfo(@PathVariable Long userId) {
    return userService.getCreditCards(userId).map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PutMapping(value = "/api/users/{userId}/creditCards", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> updatePaymentInfo(@PathVariable Long userId,
      @Valid @RequestBody PaymentMethodUpdateRequests updateRequest,
      BindingResult bindingResult) {
    if (updateRequest == null || bindingResult.hasErrors()) {
      return handleError(bindingResult, CREDIT_CARD_INPUT_ERROR);
    } else {
      try {
        userService.updatePaymentMethod(userId, updateRequest);
        return ResponseEntity.ok().build();
      } catch (UserNotFoundException une) {
        return ResponseEntity.notFound().build();
      }
    }
  }
}

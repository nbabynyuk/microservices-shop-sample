package com.example.UserApp.web;

import static com.example.UserApp.AppConstants.USER_INPUT_ERROR;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@RunWith(MockitoJUnitRunner.class)
public class DefaultErrorHandlerTest {

  @Test
  public void testHandleErrors() throws Exception {

    FieldError error = new FieldError("testObject", "testField", "testField is mandatory");

    BindingResult bindingResult = mock(BindingResult.class);
    when(bindingResult.getAllErrors()).thenReturn(asList(error));

    DefaultErroHandler handler = new DefaultErroHandler() {};
    ResponseEntity<?> response = handler.handleError(bindingResult, USER_INPUT_ERROR);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    String errorObj = response.getHeaders().get("errors").get(0);
    assertNotNull(errorObj);

    JsonNode node = new ObjectMapper().readTree(errorObj);
    assertEquals(USER_INPUT_ERROR, node.get("errorCode").asText());
    assertTrue(node.get("fieldErrors").isArray());

  }

}

package com.example.UserApp.web;

import com.example.UserApp.entity.UserEntity;
import com.example.UserApp.errors.PasswordMismatchException;
import com.example.UserApp.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
public class UsersControllerTest {

  @Mock
  private UserService userService;

  private MockMvc mockMvc;

  @Before
  public void init() {
    MockitoAnnotations.initMocks(this.getClass());
    this.mockMvc = MockMvcBuilders.standaloneSetup(new UsersController(userService))
        .setControllerAdvice(new ErrorHandlerController(new ObjectMapper()))
        .build();
  }

  @Test
  public void createUser_success() throws Exception {
    UserEntity created = new UserEntity();
    created.setId(5L);
    given(userService.createUser(any ())).willReturn(created);

    this.mockMvc.perform(post("/api/users/registration")
        .content("{"
            + "\"username\":\"client_1\","
            + "\"password\":\"p12345!\","
            + "\"pwdConfirmation\":\"p12345!\""
            + "}")
        .accept(MediaType.APPLICATION_JSON_VALUE)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .header("location", "/api/users/1"))
        .andExpect(status().isCreated());
  }

  @Test
  public void createUser_validation_failed() throws Exception {
    given(userService.createUser(any ()))
        .willThrow(new PasswordMismatchException());
    this.mockMvc.perform(post("/api/users/registration")
        .content("{"
            + "\"username\":\"client_1\","
            + "\"password\":\"p12345!\","
            + "\"pwdConfirmation\": \"pwd\""
            + "}")
        .accept(MediaType.APPLICATION_JSON_VALUE)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isBadRequest())
        .andExpect(header().exists("errors"));
  }
}

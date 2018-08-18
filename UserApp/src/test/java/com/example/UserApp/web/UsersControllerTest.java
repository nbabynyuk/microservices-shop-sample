package com.example.UserApp.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.UserApp.AppTestConfig;
import com.example.UserApp.dto.UserRegistrationRequest;
import com.example.UserApp.entity.UserEntity;
import com.example.UserApp.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppTestConfig.class)
@WebAppConfiguration
public class UsersControllerTest {

  @Autowired
  private UsersController usersController;

  @MockBean
  private UserService userService;

  private MockMvc mockMvc;

  @Before
  public void init() {
    this.mockMvc = MockMvcBuilders.standaloneSetup(usersController).build();
  }

  @Test
  public void createUser_success() throws Exception {
    UserEntity created = new UserEntity();
    created.setId(5L);
    given(userService.createUser(any ())).willReturn(created);

    UserRegistrationRequest urr = new UserRegistrationRequest();
    urr.setUsername("client_1");
    urr.setPassword("p12345!");
    urr.setPwdConfirmation("p12345!");

    ObjectMapper mapper = new ObjectMapper();
    String urrAsString = mapper.writeValueAsString(urr);
    this.mockMvc.perform(post("/api/users/registration")
        .content(urrAsString)
        .accept(MediaType.APPLICATION_JSON_VALUE)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .header("location", "/api/users/1"))
        .andExpect(status().isCreated());
  }


  @Test
  public void createUser_validation_failed() throws Exception {

    UserRegistrationRequest urr = new UserRegistrationRequest();
    urr.setUsername("client_1");
    urr.setPassword("p12345!");

    ObjectMapper mapper = new ObjectMapper();
    String urrAsString = mapper.writeValueAsString(urr);
    this.mockMvc.perform(post("/api/users/registration")
        .content(urrAsString)
        .accept(MediaType.APPLICATION_JSON_VALUE)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isBadRequest());
  }

}

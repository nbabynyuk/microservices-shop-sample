package com.example.UserApp.integration;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.UserApp.dto.LoginRequest;
import com.example.UserApp.dto.UserRegistrationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class IntegrationTest {

  @Autowired
  private WebApplicationContext webApplicationContext;
  private MockMvc mockMvc;
  private ObjectMapper mapper = new ObjectMapper();
  private static final String REF_USERNAME = "test-username";
  private static final String REF_PASSWORD = "p12345";

  @Before
  public void init() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
  }

  @Test
  public void sc1_register_and_login() throws Exception {

    UserRegistrationRequest urr = new UserRegistrationRequest(
        REF_USERNAME,
        REF_PASSWORD,
        REF_PASSWORD
    );


    String s = mapper.writeValueAsString(urr);
    this.mockMvc.perform(
        post("/api/users/registration")
            .header("Content-Type", "application/json")
        .content(s)
    ).andExpect(status().isCreated());

    LoginRequest lr = new LoginRequest(REF_USERNAME, REF_PASSWORD);
    String loginRq = mapper.writeValueAsString(lr);

    this.mockMvc.perform(
        post("/api/login")
            .header("Content-Type", "application/json")
            .content(loginRq)
    ).andExpect(status().isOk());

  }

}

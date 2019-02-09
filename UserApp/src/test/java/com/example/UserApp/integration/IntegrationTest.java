package com.example.UserApp.integration;


import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.UserApp.AppTestConfig;
import com.example.UserApp.dto.UserRegistrationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Base64;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration (classes = {AppTestConfig.class})
@WebAppConfiguration
@Profile("test")
public class IntegrationTest {

  @Autowired
  private WebApplicationContext webApplicationContext;

  private MockMvc mockMvc;

  private ObjectMapper mapper = new ObjectMapper();
  private static final String REF_USERNAME = "test-username";
  private static final String REF_PASSWORD = "p12345";

  @Before
  public void init() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
        .apply(springSecurity())
        .build();
  }

  @Test
  public void sc1_register_and_login() throws Exception {

    UserRegistrationRequest urr = new UserRegistrationRequest(
        REF_USERNAME,
        REF_PASSWORD,
        REF_PASSWORD
    );

    String s = mapper.writeValueAsString(urr);
    MvcResult registrationResult = this.mockMvc.perform(
        post("/api/users/registration")
            .header("Content-Type", "application/json")
        .content(s)
    ).andExpect(status().isCreated())
      .andReturn();

    MvcResult loginResult = this.mockMvc.perform(
        post("/api/login")
            .header("Content-Type", "application/json")
            .content("{\n"
                + " \"username\": \"" + REF_USERNAME  + "\" ,"
                + " \"password\": \"" + REF_PASSWORD + "\" "
                + "}")
    ).andExpect(status().isOk())
        .andReturn();
    String authHeader = loginResult.getResponse().getHeader("Authorization");

    String newUsersPath = registrationResult.getResponse().getHeader("location");

    this.mockMvc.perform(
        put(newUsersPath + "/creditCards")
            .header("Content-Type", "application/json")
            .header("Authorization", authHeader)
            .content("{\n"
                + "\t\"operation\":\"ADD\"\n"
                + "\t,\"creditCard\": {\n"
                + "          \"cardNumber\":\"1111-1111-1111-1111\",\n"
                + "          \"expireAt\":\"10/18\",\n"
                + "          \"cvcode\":\"111\"\n"
                + "    }\n"
                + "}")
    ).andExpect(status().isOk());

  }

}

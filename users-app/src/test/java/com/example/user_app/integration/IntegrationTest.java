package com.example.user_app.integration;

import com.example.user_app.dto.UserRegistrationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class IntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  private final ObjectMapper mapper = new ObjectMapper();
  private static final String REF_USERNAME = "test-username";
  private static final String REF_PASSWORD = "p12345";

  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
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
        post("/api/users/")
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

package com.example.UserApp.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.UserApp.AppConstants;
import com.example.UserApp.dto.LoginRequest;
import com.example.UserApp.entity.SecurityRole;
import com.example.UserApp.service.AuthProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringJUnit4ClassRunner.class)
public class LoginControllerTest {

  @Mock
  private AuthProvider authProvider;

  private MockMvc mockMvc;

  private static final String REFERENCE_USERNAME = "testedUser";

  @Before
  public void init() {
    this.mockMvc = MockMvcBuilders.standaloneSetup(new LoginController(authProvider)).build();
  }

  @Test
  public void testMainPage() throws Exception {
    mockMvc.perform(
        get("/").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json("{'name': 'UserApp', 'version':'1.0'}"));

  }

  @Test
  public void testLogin() throws Exception {

    Authentication a = new UsernamePasswordAuthenticationToken(REFERENCE_USERNAME,
        "some_pwd_hash",
        Collections.singletonList(new SecurityRole(1L, AppConstants.USER_ROLE_NAME)));

    when(authProvider.authenticate( any() )).thenReturn(a);

    LoginRequest r = new LoginRequest("test-username", "some-password");

    ObjectMapper mapper = new ObjectMapper();
    String s = mapper.writeValueAsString(r);

    mockMvc.perform(
        post("/api/login")
          .content(s)
          .header("Content-Type", "application/json")
    ).andExpect(status().isOk());
  }

}

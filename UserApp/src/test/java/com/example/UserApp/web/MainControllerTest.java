package com.example.UserApp.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringJUnit4ClassRunner.class)
public class MainControllerTest {


  private MockMvc mockMvc;

  private static final String REFERENCE_USERNAME = "testedUser";

  @Before
  public void init() {
    this.mockMvc = MockMvcBuilders.standaloneSetup(new MainController()).build();
  }

  @Test
  public void testMainPage() throws Exception {
    mockMvc.perform(
        get("/").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json("{'name': 'UserApp', 'version':'1.0'}"));

  }
}

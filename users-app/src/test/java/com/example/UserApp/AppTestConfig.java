package com.example.UserApp;

import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;

@TestConfiguration
@ComponentScan("com.example.UserApp")
public class AppTestConfig {

  public AppTestConfig(){
    MockitoAnnotations.initMocks(this);
  }
}

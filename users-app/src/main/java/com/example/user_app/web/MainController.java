package com.example.user_app.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class MainController {

  private final String appName;

  @Autowired
  public MainController(@Value("${spring.application.name}") String appName) {
    this.appName = appName;
  }

  @GetMapping(value = "/", produces = "application/json")
  public Map<String, String> index() {
    Map<String, String> m = new HashMap<>();
    m.put("name", appName);
    m.put("version", "1.0");
    return m;
  }
}

package com.example.UserApp.web;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

  @Autowired
  public MainController() {
  }

  @GetMapping(value = "/", produces = "application/json")
  public Map<String, String> index() {
    Map<String, String> m = new HashMap<>();
    m.put("name", "UserApp");
    m.put("version", "1.0");
    return m;
  }

}

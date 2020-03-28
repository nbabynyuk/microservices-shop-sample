package com.example.user_app.dto;


import javax.validation.constraints.NotEmpty;

public class LoginRequest {

  @NotEmpty
  private String username;

  @NotEmpty
  private String password;

  LoginRequest() {}

  public  LoginRequest(String u, String p) {
    this.username = u;
    this.password = p;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

}

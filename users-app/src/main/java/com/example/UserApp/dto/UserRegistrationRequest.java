package com.example.UserApp.dto;


import javax.validation.constraints.NotEmpty;

public class UserRegistrationRequest extends LoginRequest {

  @NotEmpty
  private String pwdConfirmation;

  public UserRegistrationRequest() {}

  public UserRegistrationRequest(String username, String pwd, String pwdConfirmation) {
    super(username, pwd);
    this.pwdConfirmation = pwdConfirmation;
  }

  public String getPwdConfirmation() {
    return pwdConfirmation;
  }

  public void setPwdConfirmation(String pwdConfirmation) {
    this.pwdConfirmation = pwdConfirmation;
  }


}

package com.example.user_app;

public class SecurityConstants {

  private SecurityConstants (){}

  public static final String SECRET = "SecretKeyToGenJWTs";
  public static final long EXPIRATION_TIME = 1000L * 60 * 60 * 3; // 3 hours
  public static final String TOKEN_PREFIX = "Bearer ";
  public static final String HEADER_STRING = "Authorization";
  public static final String SIGN_UP_URL = "/api/users/registration";
}
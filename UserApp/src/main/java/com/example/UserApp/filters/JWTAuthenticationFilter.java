package com.example.UserApp.filters;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.example.UserApp.SecurityConstants.EXPIRATION_TIME;
import static com.example.UserApp.SecurityConstants.HEADER_STRING;
import static com.example.UserApp.SecurityConstants.SECRET;
import static com.example.UserApp.SecurityConstants.TOKEN_PREFIX;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.Data;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
    setAuthenticationManager(authenticationManager);
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest req,
      HttpServletResponse res) throws AuthenticationException {
    try {
      AuthRequest creds = new ObjectMapper()
          .readValue(req.getInputStream(), AuthRequest.class);
      return getAuthenticationManager().authenticate(
          new UsernamePasswordAuthenticationToken(
              creds.getUsername(),
              creds.getPassword(),
              new ArrayList<>())
      );
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest req,
      HttpServletResponse res,
      FilterChain chain,
      Authentication auth) {

    String token = JWT.create()
        .withSubject(((User) auth.getPrincipal()).getUsername())
        .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
        .sign(HMAC512(SECRET.getBytes()));
    res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
  }

  @Data
  private static class AuthRequest {
    private String username;
    private String password;
  }

}
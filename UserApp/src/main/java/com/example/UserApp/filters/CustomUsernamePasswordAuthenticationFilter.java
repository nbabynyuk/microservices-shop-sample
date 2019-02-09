package com.example.UserApp.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class CustomUsernamePasswordAuthenticationFilter extends
    UsernamePasswordAuthenticationFilter {

  private Logger logger = LoggerFactory.getLogger(CustomUsernamePasswordAuthenticationFilter.class);

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException {
    try {
      BufferedReader reader = request.getReader();
      StringBuilder sb = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
        sb.append(line);
      }
      String parsedReq = sb.toString();

      ObjectMapper mapper = new ObjectMapper();
      AuthReq authReq = mapper.readValue(parsedReq, AuthReq.class);
      return new UsernamePasswordAuthenticationToken(authReq.getUsername(), authReq.getPassword());

    } catch (IOException e) {
      throw new InternalAuthenticationServiceException(
          "Failed to parse authentication request body");
    }
  }

  @Data
  public static class AuthReq {

    String username;
    String password;
  }
}

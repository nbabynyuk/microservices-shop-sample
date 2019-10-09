package com.example.UserApp.filters;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Test;
import org.springframework.mock.web.DelegatingServletInputStream;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

public class JWTAuthenticationFilterTests {


  @Test
  public void testCredentialConsumption() throws Exception {
    AuthenticationManager manager = mock(AuthenticationManager.class);

    JWTAuthenticationFilter f = new JWTAuthenticationFilter(manager);

    String body = "{ "
        + " \"username\": \"test21\","
        + " \"password\" : \"p12345\" "
        + "}";

    HttpServletRequest rq = mock(HttpServletRequest.class);
    HttpServletResponse rs = mock(HttpServletResponse.class);

    when(rq.getInputStream())
        .thenReturn(new DelegatingServletInputStream(new ByteArrayInputStream(body.getBytes())));

    Authentication a = f.attemptAuthentication(rq, rs);
    verify(manager).authenticate(eq(new UsernamePasswordAuthenticationToken(
         "test21",
        "p12345",
        new ArrayList<>())));

  }

}

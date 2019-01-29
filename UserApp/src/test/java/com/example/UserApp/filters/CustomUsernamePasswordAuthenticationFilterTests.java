package com.example.UserApp.filters;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Test;
import org.springframework.security.core.Authentication;

public class CustomUsernamePasswordAuthenticationFilterTests {


  @Test
  public void testCredentialConsumption() throws Exception {

    CustomUsernamePasswordAuthenticationFilter f = new CustomUsernamePasswordAuthenticationFilter();

    String body = "{ "
        + " \"username\": \"test21\","
        + " \"password\" : \"p12345\" "
        + "}";



    HttpServletRequest rq = mock(HttpServletRequest.class);
    HttpServletResponse rs = mock(HttpServletResponse.class);

    when(rq.getReader()).thenReturn(
        new BufferedReader(
            new InputStreamReader(
                new ByteArrayInputStream(body.getBytes()))));

    Authentication a = f.attemptAuthentication(rq, rs);
    assertEquals(a.getPrincipal() , "test21");
    assertEquals(a.getCredentials().toString(), "p12345");

  }

}

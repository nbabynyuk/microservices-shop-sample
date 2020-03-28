package com.example.user_app.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.user_app.SecurityConstants;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.user_app.SecurityConstants.HEADER_STRING;
import static com.example.user_app.SecurityConstants.TOKEN_PREFIX;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

  public JWTAuthorizationFilter(AuthenticationManager authManager) {
    super(authManager);
  }

  @Override
  protected void doFilterInternal(HttpServletRequest req,
      HttpServletResponse res,
      FilterChain chain) throws IOException, ServletException {
    String header = req.getHeader(HEADER_STRING);

    if (header == null || !header.startsWith(TOKEN_PREFIX)) {
      chain.doFilter(req, res);
      return;
    }

    UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

    SecurityContextHolder.getContext().setAuthentication(authentication);
    chain.doFilter(req, res);
  }

  private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
    String token = request.getHeader(HEADER_STRING);
    if (token != null) {
      DecodedJWT jwtToken  = JWT.require(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()))
          .build()
          .verify(token.replace(TOKEN_PREFIX, ""));
      if (jwtToken != null) {
        final String rolesAsString = jwtToken.getClaim("roles").asString();
        List<GrantedAuthority> assignedRoles = List.of(rolesAsString.split(","))
            .stream()
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toUnmodifiableList());
        return new UsernamePasswordAuthenticationToken(jwtToken.getSubject(), null, assignedRoles);
      }
      return null;
    }
    return null;
  }
}

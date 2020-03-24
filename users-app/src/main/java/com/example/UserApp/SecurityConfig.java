
package com.example.UserApp;

import com.example.UserApp.filters.JWTAuthenticationFilter;
import com.example.UserApp.filters.JWTAuthorizationFilter;
import com.example.UserApp.repo.SecurityRoleRepo;
import com.example.UserApp.repo.UserRepository;
import com.example.UserApp.service.UserService;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;


@EnableWebSecurity
@EnableAspectJAutoProxy
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public UserService userDetailsService(UserRepository ur, SecurityRoleRepo srr) {
    return  new UserService(passwordEncoder(), ur, srr);
  }

  private final AccessDeniedHandler accessDeniedHandler = (request, response, accessDeniedException) -> {
    response.getOutputStream().print("{ \"message\" : \"Authentication failure - access forbidden\"}");
    response.setStatus(HttpStatus.FORBIDDEN.value());
  };

  private final AuthenticationEntryPoint restAuthenticationEntryPoint = (request, response, authException) ->
      response.sendError(HttpStatus.UNAUTHORIZED.value(), "{ \"message\" : \"Resource requires authorization \" }");

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .sessionManagement().disable()
        .csrf().disable()
        .exceptionHandling()
          .accessDeniedHandler(accessDeniedHandler)
          .authenticationEntryPoint(restAuthenticationEntryPoint)
        .and()
          .authorizeRequests()
//          .requestMatchers(EndpointRequest.to(MetricsEndpoint.class)).permitAll()
          .mvcMatchers("/actuator/**").hasRole("ADMIN")
          .mvcMatchers(HttpMethod.POST, "/api/users")
            .permitAll()
          .mvcMatchers("/",
            "/api/login",
            "/api/logout")
          .permitAll()
          .antMatchers("/api/users/**").authenticated()
        .and()
          .addFilter(customAuthFilter())
          .addFilter(new JWTAuthorizationFilter(authenticationManager()))
        .httpBasic();
  }

  @Bean
  public JWTAuthenticationFilter customAuthFilter() throws Exception {
    JWTAuthenticationFilter f = new JWTAuthenticationFilter(authenticationManager());
    f.setFilterProcessesUrl("/api/login");
    return f;
  }
}

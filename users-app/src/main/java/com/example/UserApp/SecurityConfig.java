
package com.example.UserApp;

import com.example.UserApp.filters.JWTAuthenticationFilter;
import com.example.UserApp.filters.JWTAuthorizationFilter;
import com.example.UserApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private UserService userService;

  @Autowired
  public SecurityConfig(UserService userService) {
    this.userService = userService;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .csrf().disable()
        .authorizeRequests()
        .antMatchers("/",
            "/api/users/registration",
            "/api/login",
            "/api/logout").permitAll()
        .antMatchers("/api/users/**").authenticated()
        .and()
        .addFilter(customAuthFilter())
        .addFilter(new JWTAuthorizationFilter(authenticationManager()))
        .formLogin()
        .loginProcessingUrl("/api/login")
        .and()
        .httpBasic();
  }

  @Bean
  public JWTAuthenticationFilter customAuthFilter() throws Exception {
    JWTAuthenticationFilter f = new JWTAuthenticationFilter(authenticationManager());
    f.setFilterProcessesUrl("/api/login");
    return f;
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userService);
  }
}

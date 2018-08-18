package com.example.UserApp.service;

import static com.example.UserApp.AppConstants.USER_ROLE_NAME;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.UserApp.AppConstants;
import com.example.UserApp.entity.SecurityRole;
import com.example.UserApp.entity.UserEntity;
import com.example.UserApp.repo.UserRepository;
import java.util.Arrays;
import java.util.HashSet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

@RunWith(MockitoJUnitRunner.class)
public class AuthProviderTest {

  @Mock
  private UserService userRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @InjectMocks
  private AuthProvider authProvider;

  private static final String REFERNCE_USERNAME = "some_user";
  private static final String REFERNCE_PWD = "raw_pwd";
  private static final String REFERNCE_PWD_HASH = "abra_cadabra";

  @Before
  public void setup() {
    PasswordEncoder pe = mock(PasswordEncoder.class);
    when(pe.matches(eq(REFERNCE_PWD), eq(REFERNCE_PWD_HASH) )).thenReturn(true);
    authProvider.setPasswordEncoder(pe);
  }

  @Test
  public void authenticate_success() {
    when(userRepository.loadUserByUsername(REFERNCE_USERNAME)).thenAnswer(invocationOnMock ->
      new User(
          REFERNCE_USERNAME,
          REFERNCE_PWD_HASH,
          new HashSet<>(
              Arrays.asList(
                  new SecurityRole(1L, USER_ROLE_NAME))))
    );
    Authentication a = new UsernamePasswordAuthenticationToken(REFERNCE_USERNAME, REFERNCE_PWD);
    authProvider.authenticate(a);
  }

  @Test(expected = BadCredentialsException.class)
  public void authenticate_failed() {
    when(userRepository.loadUserByUsername(REFERNCE_USERNAME)).thenAnswer(invocationOnMock ->
        new User(
            REFERNCE_USERNAME,
            REFERNCE_PWD_HASH,
            new HashSet<>(
                Arrays.asList(
                    new SecurityRole(1L, USER_ROLE_NAME))))
    );
    Authentication a = new UsernamePasswordAuthenticationToken(REFERNCE_USERNAME, "invalid_pwd");
    authProvider.authenticate(a);
  }

}

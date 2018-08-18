package com.example.UserApp.service;

import static com.example.UserApp.AppConstants.USER_ROLE_NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.example.UserApp.AppConstants;
import com.example.UserApp.dto.UserRegistrationRequest;
import com.example.UserApp.entity.SecurityRole;
import com.example.UserApp.entity.UserEntity;
import com.example.UserApp.errors.PasswordMismatchException;
import com.example.UserApp.repo.SecurityRoleRepo;
import com.example.UserApp.repo.UserRepository;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Example;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private SecurityRoleRepo securityRoleRepo;

  @InjectMocks
  private UserService userService;

  @Test
  public void testUserCreate_success() {
    final String username = "testUsername";
    final String pwd = "some_password";
    final Long userId = 5L;

    UserRegistrationRequest urr = createSampleURR(username, pwd, pwd);

    when(passwordEncoder.encode(eq(pwd)))
        .thenReturn("some_encoded-password");
    when(userRepository.save(any()))
        .thenAnswer(invocationOnMock ->{
          UserEntity u = new UserEntity();
          u.setId(userId);
          return u;
        });

    when(securityRoleRepo.findOne(any(Example.class)))
        .thenReturn(
            Optional.of(
                new SecurityRole(1L, USER_ROLE_NAME)));

    UserEntity persisted = userService.createUser(urr);
    assertEquals(userId, persisted.getId());
  }

  @Test(expected = PasswordMismatchException.class)
  public void testUserCreate_password_mismatch() {
    final String username = "testUsername";
    final String pwd = "some_password";
    final String pwdConfirmation = "some_password1";

    UserRegistrationRequest urr = createSampleURR(username, pwd, pwdConfirmation);
    userService.createUser(urr);
  }


  @Test
  public void testLoadUserByUsername_success() {
    when(userRepository.findOne(any(Example.class))).then(invocationOnMock -> {
      UserEntity ue = new UserEntity(
          "testUserName",
          "some-secret=pwd-hash",
          Arrays.asList(
                  new SecurityRole(1L, USER_ROLE_NAME)));
      return Optional.of(ue);
    });

    UserDetails user = userService.loadUserByUsername("test");
    assertNotNull(user);
  }

  @Test(expected = UsernameNotFoundException.class)
  public void testLoadUserByUsername_userNotFound() {
    when(userRepository.findOne(any(Example.class))).thenReturn(Optional.empty());
    userService.loadUserByUsername("test");
  }

  private UserRegistrationRequest createSampleURR(String username, String pwd,
      String pwdConfirmation) {
    UserRegistrationRequest urr = new UserRegistrationRequest();
    urr.setUsername(username);
    urr.setPassword(pwd);
    urr.setPwdConfirmation(pwdConfirmation);
    return urr;
  }

}

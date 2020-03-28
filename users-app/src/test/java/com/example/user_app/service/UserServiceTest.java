package com.example.user_app.service;

import static com.example.user_app.AppConstants.USER_ROLE_NAME;
import static com.example.user_app.dto.CreditCardOperations.ADD;
import static com.example.user_app.dto.CreditCardOperations.REMOVE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.user_app.dto.PaymentMethodUpdateRequests;
import com.example.user_app.dto.UserRegistrationRequest;
import com.example.user_app.entity.CreditCard;
import com.example.user_app.entity.SecurityRole;
import com.example.user_app.entity.UserEntity;
import com.example.user_app.errors.PasswordMismatchException;
import com.example.user_app.errors.UserNotFoundException;
import com.example.user_app.repo.SecurityRoleRepo;
import com.example.user_app.repo.UserRepository;
import com.nb.common.CreditCardDTO;
import java.util.Arrays;
import java.util.List;
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


  @Test()
  public void getCreditCards() {
    final Long userId = 5L;
    final String refCardNumber = "xx";
    UserEntity u = mock(UserEntity.class);
    when(u.getCreditCards()).thenReturn(Arrays.asList(new CreditCard(refCardNumber, "11/09", "22")));

    when(userRepository.findById(userId)).thenReturn(Optional.of(u));

    Optional<List<CreditCardDTO>> cards = userService.getCreditCards(userId);
    assertTrue(cards.isPresent());
    assertEquals(1, cards.get().size());
    assertEquals(refCardNumber, cards.get().get(0).getCardNumber());
  }

  @Test()
  public void getCreditCards_user_not_found() {
    final Long userId = 5L;
    when(userRepository.findById(userId)).thenReturn(Optional.empty());
    Optional<List<CreditCardDTO>> cards = userService.getCreditCards(userId);
    assertFalse(cards.isPresent());
  }

  @Test
  public void add_credit_card_successfully() throws UserNotFoundException {
    long referenceUserId = 5L;
    UserEntity u = new UserEntity();
    CreditCardDTO cc = new CreditCardDTO("xxx", "01/01", "111");
    PaymentMethodUpdateRequests updateRequests = new PaymentMethodUpdateRequests(ADD, cc);
    when(userRepository.findById(referenceUserId)).thenReturn(Optional.of(u));
    userService.updatePaymentMethod(referenceUserId, updateRequests);
    assertEquals(1, u.getCreditCards().size());
    verify(userRepository).save(any());
  }

  @Test
  public void update_credit_card_successfully() throws UserNotFoundException {
    long referenceUserId = 5L;
    UserEntity u = new UserEntity();
    CreditCard existing = new CreditCard("xxx", "01/01", "111");
    u.addCreditCard(existing);
    CreditCardDTO updated = new CreditCardDTO("xxx", "02/02", "222");

    PaymentMethodUpdateRequests updateRequests = new PaymentMethodUpdateRequests(ADD, updated);
    when(userRepository.findById(referenceUserId)).thenReturn(Optional.of(u));
    userService.updatePaymentMethod(referenceUserId, updateRequests);
    assertEquals(1, u.getCreditCards().size());
    assertTrue(u.getCreditCards().stream().allMatch( cc -> cc.getCvcode().equals("222")));
    verify(userRepository).save(any());
  }

  @Test
  public void remove_credit_card_successfully() throws UserNotFoundException {
    long referenceUserId = 5L;
    UserEntity u = new UserEntity();
    CreditCard existing = new CreditCard("xxx", "01/01", "111");
    u.addCreditCard(existing);
    CreditCardDTO updated = new CreditCardDTO("xxx", "02/02", "222");
    PaymentMethodUpdateRequests updateRequests = new PaymentMethodUpdateRequests(REMOVE, updated);
    when(userRepository.findById(referenceUserId)).thenReturn(Optional.of(u));
    userService.updatePaymentMethod(referenceUserId, updateRequests);
    assertEquals(0, u.getCreditCards().size());
    verify(userRepository).save(any());
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

package com.example.user_app.service;

import static com.example.user_app.AppConstants.USER_ROLE_NAME;

import com.example.user_app.dto.CreditCardOperations;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

public class UserService implements UserDetailsService {

  private Logger logger = LoggerFactory.getLogger(UserService.class);

  private PasswordEncoder passwordEncoder;

  private UserRepository userRepository;

  private SecurityRoleRepo securityRoleRepo;

  @Autowired
  public UserService(PasswordEncoder pe, UserRepository ur, SecurityRoleRepo rolesRepo) {
    this.passwordEncoder = pe;
    this.userRepository = ur;
    this.securityRoleRepo = rolesRepo;
  }

  public UserEntity createUser(UserRegistrationRequest urr) throws PasswordMismatchException {
    if (urr.getPassword().equals(urr.getPwdConfirmation())) {
      return findSecurityRoleByName(USER_ROLE_NAME).map(securityRole -> {
        String pwdHash = passwordEncoder.encode(urr.getPassword());
        UserEntity u = new UserEntity(
            urr.getUsername(),
            pwdHash,
            Collections.singletonList(securityRole));
        return userRepository.save(u);
      }).orElseThrow(() -> new IllegalArgumentException("PLease check app configuration"));
    } else {
      logger.info("password and password confirmation doesn't match");
      throw new PasswordMismatchException();
    }
  }

  @Override
  public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
    UserEntity u = new UserEntity();
    u.setUsername(s);
    Example<UserEntity> ue = Example.of(u);
    Optional<UserEntity> result = userRepository.findOne(ue);
    if (result.isPresent()) {
      UserEntity userEntity = result.get();
      return new User(
          userEntity.getUsername(),
          userEntity.getPwdHash(),
          userEntity.getRoles());
    } else {
      throw new UsernameNotFoundException(s);
    }
  }

  private Optional<SecurityRole> findSecurityRoleByName(String name) {
    SecurityRole sample = new SecurityRole();
    sample.setAuthority(name);
    return securityRoleRepo.findOne(Example.of(sample));
  }

  @Transactional
  public Optional<List<CreditCardDTO>> getCreditCards(Long userId) {
    return userRepository.findById(userId).map(userEntity ->
        userEntity.getCreditCards().stream().map(dbObj ->
            new CreditCardDTO(dbObj.getCardNumber(),
                dbObj.getExpireAt(),
                dbObj.getCvcode())
        ).collect(Collectors.toList())
    );
  }

  @Transactional
  public void updatePaymentMethod(Long userId, PaymentMethodUpdateRequests r) {
    Optional<UserEntity> existingUser = userRepository.findById(userId);
    if (existingUser.isPresent()) {
      final CreditCardDTO newCard = r.getCreditCard();
      Optional<CreditCard> existingCard = findWhetherCardAlreadyExists(existingUser.get(), newCard);
      if (existingCard.isPresent()) {
        modifyUserCard(r, existingUser.get(), existingCard.get());
      } else {
        createNewCard(r, existingUser.get(), newCard);
      }
      userRepository.save(existingUser.get());
    } else {
      throw new UserNotFoundException();
    }
  }

  private Optional<CreditCard> findWhetherCardAlreadyExists(UserEntity user, CreditCardDTO newCard) {
    return user.getCreditCards()
        .stream()
        .filter(c -> c.getCardNumber().equals(newCard.getCardNumber()))
        .findAny();
  }

  private void createNewCard(PaymentMethodUpdateRequests r,
                             UserEntity user,
                             CreditCardDTO newCard) {
    if (CreditCardOperations.ADD == r.getOperation()) {
      user.getCreditCards().add(new CreditCard(
          newCard.getCardNumber(),
          newCard.getExpireAt(),
          newCard.getCvcode()));
    } else {
      logger.warn("user {} required to delete non-existing card:",
          user.getUsername());
    }
  }

  private void modifyUserCard(PaymentMethodUpdateRequests r, UserEntity user, CreditCard card) {
    if (CreditCardOperations.ADD == r.getOperation()) {
      card.setExpireAt(r.getCreditCard().getExpireAt());
      card.setCvcode(r.getCreditCard().getCvcode());
    } else if (CreditCardOperations.REMOVE == r.getOperation()) {
      user.getCreditCards().remove(card);
    } else {
      throw new IllegalArgumentException();
    }
  }
}

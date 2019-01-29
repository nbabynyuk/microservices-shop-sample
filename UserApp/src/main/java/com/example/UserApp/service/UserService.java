package com.example.UserApp.service;

import static com.example.UserApp.AppConstants.USER_ROLE_NAME;

import com.example.UserApp.dto.CreditCardOperations;
import com.example.UserApp.dto.PaymentMethodUpdateRequests;
import com.example.UserApp.dto.UserRegistrationRequest;
import com.example.UserApp.entity.CreditCard;
import com.example.UserApp.entity.SecurityRole;
import com.example.UserApp.entity.UserEntity;
import com.example.UserApp.errors.PasswordMismatchException;
import com.example.UserApp.errors.UserNotFoundException;
import com.example.UserApp.repo.SecurityRoleRepo;
import com.example.UserApp.repo.UserRepository;
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
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
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
      return findSecurityRole(USER_ROLE_NAME).map(securityRole -> {
        String pwdHash = passwordEncoder.encode(urr.getPassword());
        UserEntity u = new UserEntity(
            urr.getUsername(),
            pwdHash,
            Collections.singletonList(securityRole));
        return userRepository.save(u);
      }).orElseThrow(() -> new IllegalArgumentException("PLease check app configuration"));
    } else {
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

  private Optional<SecurityRole> findSecurityRole(String name) {
    SecurityRole sample = new SecurityRole();
    sample.setAuthority(USER_ROLE_NAME);
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
  public void updatePaymentMethod(Long userId, PaymentMethodUpdateRequests r)
      throws UserNotFoundException {
    userRepository.findById(userId).map(user -> {
      final  CreditCardDTO card = r.getCreditCard();
      Optional<CreditCard> existingCard = user.getCreditCards()
          .stream()
          .filter( c -> c.getCardNumber().equals(card.getCardNumber()))
          .findFirst();
      if (CreditCardOperations.ADD == r.getOperation()) {
        existingCard.map( c -> {
          c.setExpireAt(card.getExpireAt());
          c.setCvcode(card.getCvcode());
          return c;
        }).orElseGet( () -> {
          CreditCard c = new CreditCard(
              card.getCardNumber(),
              card.getExpireAt(),
              card.getCvcode());
          user.getCreditCards().add(c);
          return c;
        });
      } else if( CreditCardOperations.REMOVE ==  r.getOperation() ) {
        existingCard.ifPresent(creditCard -> user.getCreditCards().remove(creditCard));
      } else {
        throw new IllegalArgumentException();
      }
      userRepository.save(user);
      return true;
    }).orElseThrow(UserNotFoundException::new);
  }
}

package com.example.UserApp.service;

import static com.example.UserApp.AppConstants.USER_ROLE_NAME;

import com.example.UserApp.dto.UserRegistrationRequest;
import com.example.UserApp.entity.SecurityRole;
import com.example.UserApp.entity.UserEntity;
import com.example.UserApp.errors.PasswordMismatchException;
import com.example.UserApp.repo.SecurityRoleRepo;
import com.example.UserApp.repo.UserRepository;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserService implements UserDetailsService {

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private SecurityRoleRepo securityRoleRepo;

  public UserEntity createUser(UserRegistrationRequest urr) throws PasswordMismatchException {
    if (urr.getPassword().equals(urr.getPwdConfirmation())) {
      return findSecurityRole(USER_ROLE_NAME).map( securityRole -> {
        String pwdHash = passwordEncoder.encode(urr.getPassword());
        UserEntity u = new UserEntity(
            urr.getUsername(),
            pwdHash,
            Arrays.asList(securityRole));
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
}

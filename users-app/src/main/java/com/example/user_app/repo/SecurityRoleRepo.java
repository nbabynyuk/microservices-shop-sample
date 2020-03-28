package com.example.user_app.repo;

import com.example.user_app.entity.SecurityRole;
import javax.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class SecurityRoleRepo  extends SimpleJpaRepository<SecurityRole, Long> {

  public SecurityRoleRepo(
      EntityManager entityManager) {
    super(SecurityRole.class, entityManager);
  }
}

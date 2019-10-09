package com.example.UserApp.repo;

import com.example.UserApp.entity.UserEntity;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository extends SimpleJpaRepository<UserEntity, Long> {

  @Autowired
  public UserRepository(EntityManager em) {
    super(UserEntity.class, em);
  }
}

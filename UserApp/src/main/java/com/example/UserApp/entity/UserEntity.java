package com.example.UserApp.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class UserEntity {

  @Id
  @GeneratedValue
  private Long id;

  @Column(unique = true)
  private String username;

  @Column
  private String pwdHash;

  @Column
  private Boolean isActive;

  @ManyToMany
  private List<SecurityRole> roles = new ArrayList<>();

  public UserEntity () {}

  public UserEntity (String username, String pwdHash, List<SecurityRole> r) {
    this.username = username;
    this.pwdHash = pwdHash;
    this.isActive = true;
    this.roles.addAll(r);
  }

  public boolean isActive() {
    return isActive;
  }

  public void setActive(boolean active) {
    isActive = active;
  }

  public Long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPwdHash() {
    return pwdHash;
  }

  public void setPwdHash(String pwdHash) {
    this.pwdHash = pwdHash;
  }

  public void setRoles(List<SecurityRole> roles) {
    this.roles = roles;
  }

  public List<SecurityRole> getRoles() {
    return roles;
  }
}

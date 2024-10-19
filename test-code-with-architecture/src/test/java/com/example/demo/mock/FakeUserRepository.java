package com.example.demo.mock;

import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.service.port.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class FakeUserRepository implements UserRepository {

  private final AtomicLong autoGeneratedId = new AtomicLong(0);
  private final List<User> users = new ArrayList<>();


  @Override
  public User getById(long id) {
    return findById(id).orElseThrow(() -> new ResourceNotFoundException("Users", id));
  }

  @Override
  public Optional<User> findById(long id) {
    return users.stream().filter(user -> user.getId().equals(id)).findFirst();
  }

  @Override
  public Optional<User> findByIdAndStatus(long id, UserStatus userStatus) {
    return users.stream().filter(user -> user.getId().equals(id) && user.getStatus() == userStatus)
        .findFirst();
  }

  @Override
  public Optional<User> findByEmailAndStatus(String email, UserStatus userStatus) {
    return users.stream()
        .filter(user -> user.getEmail().equals(email) && user.getStatus() == userStatus)
        .findFirst();
  }

  @Override
  public User save(User user) {
    if (user.getId() == null || user.getId() == 0) {
      user = User.builder()
          .id(autoGeneratedId.incrementAndGet())
          .email(user.getEmail())
          .nickname(user.getNickname())
          .address(user.getAddress())
          .certificationCode(user.getCertificationCode())
          .status(user.getStatus())
          .lastLoginAt(user.getLastLoginAt())
          .build();
    } else {
      User finalUser = user;
      users.removeIf(data -> data.getId().equals(finalUser.getId()));
    }
    users.add(user);
    return user;
  }
}

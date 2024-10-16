package com.example.demo.user.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.demo.user.domain.UserStatus;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Sql("/sql/user-repository-test-data.sql")
class UserRepositoryTest {

  @Autowired
  private UserJpaRepository userRepository;


  @Test
  void findByIdAndStatus_메서드로_유저_정보_찾기() {
    Optional<UserEntity> result = userRepository.findByIdAndStatus(1L, UserStatus.ACTIVE);
    assertThat(result.isPresent()).isTrue();
  }

  @Test
  void findByIdAndStatus_메서드에_데이터가_없으면_Optional_empty() {
    Optional<UserEntity> result = userRepository.findByIdAndStatus(1L, UserStatus.PENDING);
    assertThat(result.isEmpty()).isTrue();
  }

  @Test
  void findByEmailAndStatus_메서드로_유저_정보_찾기() {
    Optional<UserEntity> result = userRepository.findByEmailAndStatus("sss3598@gmail.com",
        UserStatus.ACTIVE);
    assertThat(result.isPresent()).isTrue();
  }

  @Test
  void findByEmailAndStatus_메서드에_데이터가_없으면_Optional_empty() {
    Optional<UserEntity> result = userRepository.findByEmailAndStatus("sss3598@gmail.com",
        UserStatus.PENDING);
    assertThat(result.isEmpty()).isTrue();
  }
}
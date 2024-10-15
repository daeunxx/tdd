package com.example.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlGroup;

@SpringBootTest
@TestPropertySource("classpath:test-application.properties")
@SqlGroup({
    @Sql(value = "/sql/user-service-test-data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD),
    @Sql(value = "/sql/delete-all-data.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
})
class UserServiceTest {

  @Autowired
  private UserService userService;

  @Test
  void getByEmail_ACTIVE_User_찾아올수있음() {
    // given
    String email = "sss3598@gmail.com";

    // when
    UserEntity result = userService.getByEmail(email);

    // then
    assertThat(result.getNickname()).isEqualTo("daeun");
  }

  @Test
  void getByEmail_PENDING_User_찾아올수없음() {
    // given
    String email = "dada@gmail.com";

    // when
    // then
    assertThatThrownBy(() -> userService.getByEmail(email)).isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  void getById_ACTIVE_User_찾아올수있음() {
    UserEntity result = userService.getById(1);
    assertThat(result.getNickname()).isEqualTo("daeun");
  }

  @Test
  void getById_PENDING_User_찾아올수없음() {
    assertThatThrownBy(() -> userService.getById(2)).isInstanceOf(ResourceNotFoundException.class);
  }
}
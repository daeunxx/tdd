package com.example.demo.medium.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.user.controller.port.UserService;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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

  @MockBean
  private JavaMailSender mailSender;

  @Test
  void getByEmail_ACTIVE_User_찾아올수있음() {
    // given
    String email = "sss3598@gmail.com";

    // when
    User result = userService.getByEmail(email);

    // then
    assertThat(result.getNickname()).isEqualTo("daeun");
  }

  @Test
  void getByEmail_PENDING_User_찾아올수없음() {
    // given
    String email = "dada@gmail.com";

    // when
    // then
    assertThatThrownBy(() -> userService.getByEmail(email)).isInstanceOf(
        ResourceNotFoundException.class);
  }

  @Test
  void getById_ACTIVE_User_찾아올수있음() {
    User result = userService.getById(1);
    assertThat(result.getNickname()).isEqualTo("daeun");
  }

  @Test
  void getById_PENDING_User_찾아올수없음() {
    assertThatThrownBy(() -> userService.getById(2)).isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  void userCreate_이용하여_유저_생성() {
    // given
    UserCreate userCreate = UserCreate.builder()
        .email("test@gmail.com")
        .address("Pangyo")
        .nickname("test")
        .build();

    BDDMockito.doNothing().when(mailSender).send(any(SimpleMailMessage.class));

    // when
    User result = userService.create(userCreate);

    // then
    assertThat(result).isNotNull();
    assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING);
    //assertThat(result.getCertificationCode()).isEqualTo();
  }

  @Test
  void userUpdate_이용하여_유저_수정() {
    // given
    UserUpdate userUpdate = UserUpdate.builder()
        .address("Gyunggi")
        .nickname("danna")
        .build();

    BDDMockito.doNothing().when(mailSender).send(any(SimpleMailMessage.class));

    // when
    User result = userService.update(1, userUpdate);

    // then
    User user = userService.getById(1);
    assertThat(user).isNotNull();
    assertThat(user.getAddress()).isEqualTo("Gyunggi");
    assertThat(user.getNickname()).isEqualTo("danna");
  }

  @Test
  void login_후_마지막_로그인_시간_수정() {
    // given
    // when
    userService.login(1);

    // then
    User user = userService.getById(1);
    assertThat(user.getLastLoginAt()).isGreaterThan(0L);
  }

  @Test
  void PENDING_상태의_사용자는_인증_코드로_ACTIVE_가능() {
    // given
    // when
    userService.verifyEmail(2, "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab");

    // then
    User user = userService.getById(2);
    assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
  }

  @Test
  void PENDING_상태의_사용자는_잘못된_인증코드_에러() {
    assertThatThrownBy(
        () -> userService.verifyEmail(2, "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaac")).isInstanceOf(
        CertificationCodeNotMatchedException.class);
    ;
  }
}
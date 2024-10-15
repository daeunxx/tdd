package com.example.demo.service;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

import com.example.demo.exception.CertificationCodeNotMatchedException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.UserStatus;
import com.example.demo.model.dto.UserCreateDto;
import com.example.demo.model.dto.UserUpdateDto;
import com.example.demo.repository.UserEntity;
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
    assertThatThrownBy(() -> userService.getByEmail(email)).isInstanceOf(
        ResourceNotFoundException.class);
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

  @Test
  void userCreateDto_이용하여_유저_생성() {
    // given
    UserCreateDto userCreateDto = UserCreateDto.builder()
        .email("test@gmail.com")
        .address("Pangyo")
        .nickname("test")
        .build();

    BDDMockito.doNothing().when(mailSender).send(any(SimpleMailMessage.class));

    // when
    UserEntity result = userService.create(userCreateDto);

    // then
    assertThat(result).isNotNull();
    assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING);
    //assertThat(result.getCertificationCode()).isEqualTo();
  }

  @Test
  void userCreateDto_이용하여_유저_수정() {
    // given
    UserUpdateDto userUpdateDto = UserUpdateDto.builder()
        .address("Gyunggi")
        .nickname("danna")
        .build();

    BDDMockito.doNothing().when(mailSender).send(any(SimpleMailMessage.class));

    // when
    UserEntity result = userService.update(1, userUpdateDto);

    // then
    UserEntity userEntity = userService.getById(1);
    assertThat(userEntity).isNotNull();
    assertThat(userEntity.getAddress()).isEqualTo("Gyunggi");
    assertThat(userEntity.getNickname()).isEqualTo("danna");
  }

  @Test
  void login_후_마지막_로그인_시간_수정() {
    // given
    // when
    userService.login(1);

    // then
    UserEntity userEntity = userService.getById(1);
    assertThat(userEntity.getLastLoginAt()).isGreaterThan(0L);
  }

  @Test
  void PENDING_상태의_사용자는_인증_코드로_ACTIVE_가능() {
    // given
    // when
    userService.verifyEmail(2, "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

    // then
    UserEntity userEntity = userService.getById(2);
    assertThat(userEntity.getStatus()).isEqualTo(UserStatus.ACTIVE);
  }

  @Test
  void PENDING_상태의_사용자는_잘못된_인증코드_에러() {
    assertThatThrownBy(
        () -> userService.verifyEmail(2, "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")).isInstanceOf(
        CertificationCodeNotMatchedException.class);
    ;
  }
}
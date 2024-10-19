package com.example.demo.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.FakeMailSender;
import com.example.demo.mock.FakeUserRepository;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUuidHolder;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import com.example.demo.user.service.port.CertificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserServiceTest {

  private UserService userService;

  @BeforeEach
  void init() {
    FakeMailSender fakeMailSender = new FakeMailSender();

    FakeUserRepository fakeUserRepository = new FakeUserRepository();
    fakeUserRepository.save(User.builder()
        .id(1L)
        .email("sss3598@gmail.com")
        .nickname("daeun")
        .address("Seoul")
        .status(UserStatus.ACTIVE)
        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
        .build());
    fakeUserRepository.save(User.builder()
        .id(2L)
        .email("dada@gmail.com")
        .nickname("dada")
        .address("Seoul")
        .status(UserStatus.PENDING)
        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
        .build());

    this.userService = UserService.builder()
        .userRepository(fakeUserRepository)
        .certificationService(new CertificationService(fakeMailSender))
        .uuidHolder(new TestUuidHolder("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"))
        .clockHolder(new TestClockHolder(1678530673958L))
        .build();
  }

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

    // when
    User result = userService.create(userCreate);

    // then
    assertThat(result).isNotNull();
    assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING);
    assertThat(result.getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
  }

  @Test
  void userUpdate_이용하여_유저_수정() {
    // given
    UserUpdate userUpdate = UserUpdate.builder()
        .address("Gyunggi")
        .nickname("danna")
        .build();

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
    userService.login(1L);

    // then
    User user = userService.getById(1L);
    assertThat(user.getLastLoginAt()).isEqualTo(1678530673958L);
  }

  @Test
  void PENDING_상태의_사용자는_인증_코드로_ACTIVE_가능() {
    // given
    // when
    userService.verifyEmail(2L, "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

    // then
    User user = userService.getById(2L);
    assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
  }

  @Test
  void PENDING_상태의_사용자는_잘못된_인증코드_에러() {
    assertThatThrownBy(
        () -> userService.verifyEmail(2L, "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaac")).isInstanceOf(
        CertificationCodeNotMatchedException.class);
    ;
  }
}
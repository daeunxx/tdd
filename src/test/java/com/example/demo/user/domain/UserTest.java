package com.example.demo.user.domain;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUuidHolder;
import org.junit.jupiter.api.Test;

class UserTest {

  @Test
  public void UserCreate_객체로_User_생성() {
    //given
    UserCreate userCreate = UserCreate.builder()
        .email("sss3598@gmail.com")
        .nickname("daeun")
        .address("Pangyo")
        .build();

    //when
    User user = User.create(userCreate, new TestUuidHolder("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"));

    //then
    assertThat(user.getEmail()).isEqualTo("sss3598@gmail.com");
    assertThat(user.getNickname()).isEqualTo("daeun");
    assertThat(user.getAddress()).isEqualTo("Pangyo");
    assertThat(user.getStatus()).isEqualTo(UserStatus.PENDING);
    assertThat(user.getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
  }

  @Test
  public void UserUpdate_객체로_User_수정() {
    //given
    User user = User.builder()
        .id(1L)
        .email("sss3598@gmail.com")
        .nickname("daeun")
        .address("Seoul")
        .status(UserStatus.ACTIVE)
        .lastLoginAt(100L)
        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
        .build();

    UserUpdate userUpdate = UserUpdate.builder()
        .nickname("dada")
        .address("Seoul")
        .build();

    //when
    user = user.update(userUpdate);

    //then
    assertThat(user.getNickname()).isEqualTo("dada");
    assertThat(user.getAddress()).isEqualTo("Seoul");
  }

  @Test
  public void User_로그인_후_마지막_로그인_시간_변경() {
    //given
    User user = User.builder()
        .id(1L)
        .email("sss3598@gmail.com")
        .nickname("daeun")
        .address("Seoul")
        .status(UserStatus.ACTIVE)
        .lastLoginAt(100L)
        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
        .build();

    //when
    user = user.login(new TestClockHolder(1678530673958L));

    //then
    assertThat(user.getLastLoginAt()).isEqualTo(1678530673958L);
  }

  @Test
  public void 유효한_인증_코드로_계정_활성화() {
    //given
    User user = User.builder()
        .id(1L)
        .email("sss3598@gmail.com")
        .nickname("daeun")
        .address("Seoul")
        .status(UserStatus.PENDING)
        .lastLoginAt(100L)
        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
        .build();

    //when
    user = user.certificate("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

    //then
    assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
  }

  @Test
  public void 유효하지_않은_인증_코드는_에러() {
    //given
    User user = User.builder()
        .id(1L)
        .email("sss3598@gmail.com")
        .nickname("daeun")
        .address("Seoul")
        .status(UserStatus.PENDING)
        .lastLoginAt(100L)
        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
        .build();

    //when
    //then
    assertThatThrownBy(() -> user.certificate("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")).isInstanceOf(
        CertificationCodeNotMatchedException.class);
  }
}
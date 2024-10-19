package com.example.demo.user.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.TestContainer;
import com.example.demo.user.controller.response.UserResponse;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class UserControllerTest {

  @Test
  void 개인정보_제거_후_조회() {
    //given
    TestContainer testContainer = TestContainer.builder()
        .build();

    User user = User.builder()
        .id(1L)
        .email("sss3598@gmail.com")
        .nickname("daeun")
        .address("Seoul")
        .status(UserStatus.ACTIVE)
        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
        .build();

    testContainer.userRepository.save(user);

    //when
    ResponseEntity<UserResponse> result = testContainer.userController.getUserById(user.getId());

    //then
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(result.getBody().getEmail()).isEqualTo(user.getEmail());
    assertThat(result.getBody().getNickname()).isEqualTo(user.getNickname());
    assertThat(result.getBody().getLastLoginAt()).isEqualTo(user.getLastLoginAt());
    assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
  }

  @Test
  void 존재하지_않는_사용자() {
    //given
    TestContainer testContainer = TestContainer.builder()
        .build();

    //when
    //then
    assertThatThrownBy(() -> testContainer.userController.getUserById(1L)).isInstanceOf(
        ResourceNotFoundException.class);
  }

  @Test
  void 인증_코드로_계정_활성화() {
    //given
    TestContainer testContainer = TestContainer.builder()
        .build();

    User user = User.builder()
        .id(1L)
        .email("sss3598@gmail.com")
        .nickname("daeun")
        .address("Seoul")
        .status(UserStatus.ACTIVE)
        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
        .build();

    testContainer.userRepository.save(user);

    //when
    ResponseEntity<Void> result = testContainer.userController.verifyEmail(1L,
        "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

    //then
    assertThat(result.getStatusCodeValue()).isEqualTo(302);
  }

  @Test
  void 잘못된_인증_코드_권한없음() {
    //given
    TestContainer testContainer = TestContainer.builder()
        .build();

    User user = User.builder()
        .id(1L)
        .email("sss3598@gmail.com")
        .nickname("daeun")
        .address("Seoul")
        .status(UserStatus.ACTIVE)
        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
        .build();

    testContainer.userRepository.save(user);

    //when
    //then
    assertThatThrownBy(() -> testContainer.userController.verifyEmail(1L,
        "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")).isInstanceOf(CertificationCodeNotMatchedException.class);
  }
}
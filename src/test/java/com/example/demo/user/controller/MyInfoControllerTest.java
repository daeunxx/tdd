package com.example.demo.user.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestContainer;
import com.example.demo.user.domain.MyProfileResponse;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class MyInfoControllerTest {

  @Test
  void 특정_유저_정보_전달() {
    //given
    TestContainer testContainer = TestContainer.builder()
        .clockHolder(new TestClockHolder(1000L))
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
    ResponseEntity<MyProfileResponse> result = testContainer.myInfoController.getMyInfo(
        "sss3598@gmail.com");

    //then
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(result.getBody().getNickname()).isEqualTo("daeun");
    assertThat(result.getBody().getLastLoginAt()).isEqualTo(1000L);
  }

  @Test
  void 존재_하지_않는_유저_정보_전달() {
    //given
    TestContainer testContainer = TestContainer.builder()
        .clockHolder(new TestClockHolder(1000L))
        .build();

    //when
    //then
    assertThatThrownBy(() -> testContainer.myInfoController.getMyInfo("test@gmail.com"))
        .isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  void 수정() {
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
    UserUpdate userUpdate = UserUpdate.builder()
        .nickname("dada")
        .address("Pangyo")
        .build();
    ResponseEntity<MyProfileResponse> result = testContainer.myInfoController.updateMyInfo(
        user.getEmail(), userUpdate);

    //then
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(result.getBody().getNickname()).isEqualTo("dada");
    assertThat(result.getBody().getAddress()).isEqualTo("Pangyo");
  }
}
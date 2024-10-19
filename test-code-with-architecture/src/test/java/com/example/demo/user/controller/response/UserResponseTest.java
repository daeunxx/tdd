package com.example.demo.user.controller.response;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

class UserResponseTest {

  @Test
  public void User_객체로_UserResponse_생성() {
    //given
    User user = User.builder()
        .id(1L)
        .email("sss3598@gmail.com")
        .nickname("daeun")
        .address("Seoul")
        .status(UserStatus.ACTIVE)
        .lastLoginAt(100L)
        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
        .build();

    //when
    UserResponse userResponse = UserResponse.from(user);

    //then
    assertThat(userResponse.getId()).isEqualTo(1L);
    assertThat(userResponse.getEmail()).isEqualTo("sss3598@gmail.com");
    assertThat(userResponse.getNickname()).isEqualTo("daeun");
    assertThat(userResponse.getStatus()).isEqualTo(UserStatus.ACTIVE);
    assertThat(userResponse.getLastLoginAt()).isEqualTo(100L);
  }
}
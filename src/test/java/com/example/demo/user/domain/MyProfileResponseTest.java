package com.example.demo.user.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class MyProfileResponseTest {

  @Test
  public void User_객체로_MyProfileResponse_생성() {
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
    MyProfileResponse myProfileResponse = MyProfileResponse.from(user);

    //then
    assertThat(myProfileResponse.getId()).isEqualTo(1L);
    assertThat(myProfileResponse.getEmail()).isEqualTo("sss3598@gmail.com");
    assertThat(myProfileResponse.getNickname()).isEqualTo("daeun");
    assertThat(myProfileResponse.getStatus()).isEqualTo(UserStatus.ACTIVE);
    assertThat(myProfileResponse.getAddress()).isEqualTo("Seoul");
    assertThat(myProfileResponse.getLastLoginAt()).isEqualTo(100L);
  }
}
package com.example.demo.user.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class MyProfileResponse {

  private Long id;
  private String email;
  private String nickname;
  private UserStatus status;
  private String address;
  private Long lastLoginAt;

  public static MyProfileResponse from(User user) {
    return MyProfileResponse.builder()
        .id(user.getId())
        .email(user.getEmail())
        .nickname(user.getNickname())
        .status(user.getStatus())
        .address(user.getAddress())
        .lastLoginAt(user.getLastLoginAt())
        .build();
  }
}

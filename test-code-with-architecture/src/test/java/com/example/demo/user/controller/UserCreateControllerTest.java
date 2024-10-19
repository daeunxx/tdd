package com.example.demo.user.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.demo.mock.TestContainer;
import com.example.demo.mock.TestUuidHolder;
import com.example.demo.user.controller.response.UserResponse;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


class UserCreateControllerTest {

  @Test
  void 사용자_생성() {
    //given
    TestContainer testContainer = TestContainer.builder()
        .uuidHolder(new TestUuidHolder("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"))
        .build();

    //when
    UserCreate userCreate = UserCreate.builder()
        .email("sss3598@gmail.com")
        .nickname("daeun")
        .address("Pangyo")
        .build();

    ResponseEntity<UserResponse> result = testContainer.userCreateController.createUser(userCreate);

    //then
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(result.getBody().getNickname()).isEqualTo("daeun");
    assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.PENDING);
  }
}
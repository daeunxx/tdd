package com.example.demo.post.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestContainer;
import com.example.demo.post.controller.response.PostResponse;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class PostCreateControllerTest {

  @Test
  void 게시물_생성() throws Exception {
    //given
    TestContainer testContainer = TestContainer.builder()
        .clockHolder(new TestClockHolder(100L))
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
    PostCreate postCreate = PostCreate.builder()
        .writerId(1)
        .content("hi")
        .build();

    ResponseEntity<PostResponse> result = testContainer.postCreateController.createPost(postCreate);

    //then
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(result.getBody().getCreatedAt()).isEqualTo(100L);
    assertThat(result.getBody().getContent()).isEqualTo("hi");
    assertThat(result.getBody().getWriter().getNickname()).isEqualTo("daeun");
  }
}
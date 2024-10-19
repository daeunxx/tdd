package com.example.demo.post.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestContainer;
import com.example.demo.post.controller.response.PostResponse;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


class PostControllerTest {

  @Test
  void 게시글_조회() {
    //given
    TestContainer testContainer = TestContainer.builder().build();
    User user = User.builder()
        .id(1L)
        .email("sss3598@gmail.com")
        .nickname("daeun")
        .address("Seoul")
        .status(UserStatus.ACTIVE)
        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
        .build();

    Post post = Post.builder()
        .id(1L)
        .content("helloWorld")
        .createdAt(100L)
        .writer(user)
        .build();

    testContainer.userRepository.save(user);
    testContainer.postRepository.save(post);

    //when
    ResponseEntity<PostResponse> result = testContainer.postController.getById(post.getId());

    //then
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(result.getBody()).isNotNull();
    assertThat(result.getBody().getContent()).isEqualTo("helloWorld");
    assertThat(result.getBody().getWriter().getNickname()).isEqualTo("daeun");
    assertThat(result.getBody().getCreatedAt()).isEqualTo(100L);
  }

  @Test
  void 존재하지_않은_게시글_조회() {
    //given
    TestContainer testContainer = TestContainer.builder().build();

    //when
    //then
    assertThatThrownBy(() -> testContainer.postController.getById(1L)).isInstanceOf(
        ResourceNotFoundException.class);

  }

  @Test
  void 게시글_수정() {
    //given
    TestContainer testContainer = TestContainer.builder()
        .clockHolder(new TestClockHolder(200L))
        .build();
    User user = User.builder()
        .id(1L)
        .email("sss3598@gmail.com")
        .nickname("daeun")
        .address("Seoul")
        .status(UserStatus.ACTIVE)
        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
        .build();

    Post post = Post.builder()
        .id(1L)
        .content("helloWorld")
        .createdAt(100L)
        .writer(user)
        .build();

    testContainer.userRepository.save(user);
    testContainer.postRepository.save(post);

    //when
    PostUpdate postUpdate = PostUpdate.builder()
        .content("hiWorld")
        .build();

    ResponseEntity<PostResponse> result = testContainer.postController.update(
        post.getId(), postUpdate);

    //then
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(result.getBody()).isNotNull();
    assertThat(result.getBody().getContent()).isEqualTo("hiWorld");
    assertThat(result.getBody().getModifiedAt()).isEqualTo(200L);
  }
}
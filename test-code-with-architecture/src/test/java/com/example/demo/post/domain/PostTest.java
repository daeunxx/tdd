package com.example.demo.post.domain;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.demo.mock.TestClockHolder;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

class PostTest {

  @Test
  public void PostCreate_객체로_Post_생성() {
    //given
    PostCreate postCreate = PostCreate.builder()
        .writerId(1)
        .content("helloWorld")
        .build();

    User writer = User.builder()
        .email("sss3598@gmail.com")
        .nickname("daeun")
        .address("Seoul")
        .status(UserStatus.ACTIVE)
        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
        .build();

    //when
    Post post = Post.create(writer, postCreate, new TestClockHolder(1678530673958L));

    //then
    assertThat(post.getCreatedAt()).isEqualTo(1678530673958L);
    assertThat(post.getContent()).isEqualTo("helloWorld");
    assertThat(post.getWriter().getEmail()).isEqualTo("sss3598@gmail.com");
    assertThat(post.getWriter().getNickname()).isEqualTo("daeun");
    assertThat(post.getWriter().getAddress()).isEqualTo("Seoul");
    assertThat(post.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
    assertThat(post.getWriter().getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
  }

  @Test
  public void PostUpdate_객체로_Post_수정() {
    //given
    PostCreate postCreate = PostCreate.builder()
        .writerId(1)
        .content("helloWorld")
        .build();

    User writer = User.builder()
        .email("sss3598@gmail.com")
        .nickname("daeun")
        .address("Seoul")
        .status(UserStatus.ACTIVE)
        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
        .build();

    Post post = Post.create(writer, postCreate, new TestClockHolder(1678530673958L));
    PostUpdate postUpdate = PostUpdate.builder()
        .content("hiWorld")
        .build();

    //when
    post = post.update(postUpdate, new TestClockHolder(1678530673958L));


    //then
    assertThat(post.getContent()).isEqualTo("hiWorld");
    assertThat(post.getModifiedAt()).isEqualTo(1678530673958L);
  }
}
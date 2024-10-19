package com.example.demo.post.controller.response;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.demo.post.domain.Post;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

class PostResponseTest {

  @Test
  public void Post_객체로_PostResponse_생성() {
    //given
    Post post = Post.builder()
        .content("helloWorld")
        .writer(User.builder()
            .email("sss3598@gmail.com")
            .nickname("daeun")
            .address("Seoul")
            .status(UserStatus.ACTIVE)
            .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
            .build())
        .build();

    //when
    PostResponse postResponse = PostResponse.from(post);

    //then
    assertThat(postResponse.getContent()).isEqualTo("helloWorld");
    assertThat(postResponse.getWriter().getEmail()).isEqualTo("sss3598@gmail.com");
    assertThat(postResponse.getWriter().getNickname()).isEqualTo("daeun");
    assertThat(postResponse.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
  }

}
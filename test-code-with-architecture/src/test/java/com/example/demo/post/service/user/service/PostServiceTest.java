package com.example.demo.post.service.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.FakePostRepository;
import com.example.demo.mock.FakeUserRepository;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.post.service.PostServiceImpl;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PostServiceTest {

  private PostServiceImpl postService;

  @BeforeEach
  void init() {
    FakePostRepository fakePostRepository = new FakePostRepository();
    FakeUserRepository fakeUserRepository = new FakeUserRepository();

    User user = fakeUserRepository.save(User.builder()
        .id(1L)
        .email("sss3598@gmail.com")
        .nickname("daeun")
        .address("Seoul")
        .status(UserStatus.ACTIVE)
        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
        .build());
    fakeUserRepository.save(User.builder()
        .id(2L)
        .email("dada@gmail.com")
        .nickname("dada")
        .address("Seoul")
        .status(UserStatus.PENDING)
        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
        .build());
    fakePostRepository.save(Post.builder()
            .id(1L)
            .content("helloWorld")
            .createdAt(1678530673958L)
            .modifiedAt(0L)
            .writer(user)
        .build());

    postService = PostServiceImpl.builder()
        .postRepository(fakePostRepository)
        .userRepository(fakeUserRepository)
        .clockHolder(new TestClockHolder(1678530673958L))
        .build();
  }

  @Test
  void getById_존재하는_게시물_조회() {
    // given
    // when
    Post result = postService.getById(1);

    // then
    assertThat(result).isNotNull();
  }

  @Test
  void getById_존재하지_않는_게시물_조회() {
    assertThatThrownBy(() -> postService.getById(2)).isInstanceOf(ResourceNotFoundException.class);
  }


  @Test
  void postCreate_이용하여_게시글_생성() {
    // given
    PostCreate postCreate = PostCreate.builder()
        .writerId(1)
        .content("hi")
        .build();

    // when
    Post post = postService.create(postCreate);

    // then
    assertThat(post).isNotNull();
    assertThat(post.getWriter().getId()).isEqualTo(1);
    assertThat(post.getContent()).isEqualTo("hi");
    assertThat(post.getCreatedAt()).isEqualTo(1678530673958L);
  }

  @Test
  void postUpdate_이용하여_게시글_수정() {
    // given
    PostUpdate postUpdate = PostUpdate.builder()
        .content("hi everyone")
        .build();

    // when
    Post post = postService.update(1, postUpdate);

    // then
    assertThat(post).isNotNull();
    assertThat(post.getContent()).isEqualTo("hi everyone");
    assertThat(post.getModifiedAt()).isEqualTo(1678530673958L);
  }
}
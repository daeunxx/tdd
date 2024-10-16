package com.example.demo.post.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.post.infrastructure.PostEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlGroup;

@SpringBootTest
@TestPropertySource("classpath:test-application.properties")
@SqlGroup({
    @Sql(value = "/sql/post-service-test-data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD),
    @Sql(value = "/sql/delete-all-data.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
})
class PostServiceTest {

  @Autowired
  private PostService postService;

  @MockBean
  private JavaMailSender mailSender;

  @Test
  void getById_존재하는_게시물_조회() {
    // given
    // when
    PostEntity result = postService.getById(1);

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
    PostEntity postEntity = postService.create(postCreate);

    // then
    assertThat(postEntity).isNotNull();
    assertThat(postEntity.getWriter().getId()).isEqualTo(1);
    assertThat(postEntity.getContent()).isEqualTo("hi");
    //assertThat(postEntity.getCreatedAt()).isGreaterThan(0);
  }

  @Test
  void postUpdate_이용하여_게시글_수정() {
    // given
    PostUpdate postUpdate = PostUpdate.builder()
        .content("hi everyone")
        .build();

    // when
    PostEntity postEntity = postService.update(1, postUpdate);

    // then
    assertThat(postEntity).isNotNull();
    assertThat(postEntity.getContent()).isEqualTo("hi everyone");
    assertThat(postEntity.getModifiedAt()).isGreaterThan(0);
  }
}
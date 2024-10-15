package com.example.demo.service;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

import com.example.demo.exception.CertificationCodeNotMatchedException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.UserStatus;
import com.example.demo.model.dto.PostCreateDto;
import com.example.demo.model.dto.PostUpdateDto;
import com.example.demo.model.dto.UserCreateDto;
import com.example.demo.model.dto.UserUpdateDto;
import com.example.demo.repository.PostEntity;
import com.example.demo.repository.UserEntity;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
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
  void postCreateDto_이용하여_게시글_생성() {
    // given
    PostCreateDto postCreateDto = PostCreateDto.builder()
        .writerId(1)
        .content("hi")
        .build();

    // when
    PostEntity postEntity = postService.create(postCreateDto);

    // then
    assertThat(postEntity).isNotNull();
    assertThat(postEntity.getWriter().getId()).isEqualTo(1);
    assertThat(postEntity.getContent()).isEqualTo("hi");
    //assertThat(postEntity.getCreatedAt()).isGreaterThan(0);
  }

  @Test
  void postUpdateDto_이용하여_게시글_수정() {
    // given
    PostUpdateDto postUpdateDto = PostUpdateDto.builder()
        .content("hi everyone")
        .build();

    // when
    PostEntity postEntity = postService.update(1, postUpdateDto);

    // then
    assertThat(postEntity).isNotNull();
    assertThat(postEntity.getContent()).isEqualTo("hi everyone");
    assertThat(postEntity.getModifiedAt()).isGreaterThan(0);
  }
}
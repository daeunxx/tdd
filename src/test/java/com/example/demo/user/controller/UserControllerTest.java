package com.example.demo.user.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import com.example.demo.user.infrastructure.UserEntity;
import com.example.demo.user.infrastructure.UserJpaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@SqlGroup({
    @Sql(value = "/sql/user-service-test-data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD),
    @Sql(value = "/sql/delete-all-data.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
})
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserJpaRepository userRepository;
  private ObjectMapper objectMapper = new ObjectMapper();

  @Test
  void 특정_유저_정보_전달() throws Exception {
    mockMvc.perform(get("/api/users/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.email").value("sss3598@gmail.com"))
        .andExpect(jsonPath("$.nickname").value("daeun"))
        .andExpect(jsonPath("$.address").doesNotExist())
        .andExpect(jsonPath("$.status").value("ACTIVE"));
  }

  @Test
  void 존재_하지_않는_유저_정보_전달() throws Exception {
    mockMvc.perform(get("/api/users/123"))
        .andExpect(status().isNotFound())
        .andExpect(content().string("Users에서 ID 123를 찾을 수 없습니다."));
  }

  @Test
  void 인증_코드로_계정_활성화() throws Exception {
    mockMvc.perform(get("/api/users/2/verify")
            .queryParam("certificationCode", "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab"))
        .andExpect(status().isFound());

    UserEntity userEntity = userRepository.findById(2L).get();
    assertThat(userEntity.getStatus()).isEqualTo(UserStatus.ACTIVE);
  }

  @Test
  void 잘못된_인증_코드_권한없음() throws Exception {
    mockMvc.perform(get("/api/users/2/verify")
            .queryParam("certificationCode", "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaac"))
        .andExpect(status().isForbidden());
  }

  @Test
  void 이메일_헤더로_주소_조회() throws Exception {
    mockMvc.perform(get("/api/users/me")
            .header("EMAIL", "sss3598@gmail.com"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.address").value("Seoul"));
  }

  @Test
  void 수정() throws Exception {
    UserUpdate userUpdate = UserUpdate.builder()
        .nickname("test")
        .address("Youngsan")
        .build();

    mockMvc.perform(put("/api/users/me")
            .header("EMAIL", "sss3598@gmail.com")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userUpdate)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.nickname").value("test"))
        .andExpect(jsonPath("$.address").value("Youngsan"));
  }
}
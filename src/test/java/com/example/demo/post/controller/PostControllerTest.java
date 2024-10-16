package com.example.demo.post.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.post.domain.PostUpdate;
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
    @Sql(value = "/sql/post-service-test-data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD),
    @Sql(value = "/sql/delete-all-data.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
})
class PostControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper = new ObjectMapper();

  @Test
  void 게시글_조회() throws Exception {
    mockMvc.perform(get("/api/posts/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.content").value("helloworld"))
        .andExpect(jsonPath("$.writer.id").value(1))
        .andExpect(jsonPath("$.writer.email").value("sss3598@gmail.com"))
        .andExpect(jsonPath("$.writer.nickname").value("daeun"))
        .andExpect(jsonPath("$.writer.status").value("ACTIVE"));

  }

  @Test
  void 존재하지_않은_게시글_조회() throws Exception {
    mockMvc.perform(get("/api/posts/1234"))
        .andExpect(status().isNotFound())
        .andExpect(content().string("Posts에서 ID 1234를 찾을 수 없습니다."));
  }

  @Test
  void 게시글_수정() throws Exception {
    PostUpdate postUpdate = PostUpdate.builder()
        .content("hiworld")
        .build();

    mockMvc.perform(put("/api/posts/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(postUpdate)))
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.content").value("hiworld"))
        .andExpect(jsonPath("$.writer.id").value(1));
  }
}
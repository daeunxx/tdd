package com.example.demo.post.service;

import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.common.service.port.ClockHolder;
import com.example.demo.post.controller.port.PostService;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.post.service.port.PostRepository;
import com.example.demo.user.domain.User;
import com.example.demo.user.service.port.UserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Builder
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

  private final PostRepository postRepository;
  private final UserRepository userRepository;
  private final ClockHolder clockHolder;

  @Override
  public Post getById(long id) {
    return postRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Posts", id));
  }

  @Override
  public Post create(PostCreate postCreate) {
    User writer = userRepository.getById(postCreate.getWriterId());
    Post post = Post.create(writer, postCreate, clockHolder);
    post = postRepository.save(post);
    return post;
  }

  @Override
  public Post update(long id, PostUpdate postUpdate) {
    Post post = getById(id);
    post = post.update(postUpdate, clockHolder);
    post = postRepository.save(post);
    return post;
  }
}
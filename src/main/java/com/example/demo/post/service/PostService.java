package com.example.demo.post.service;

import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.common.service.port.ClockHolder;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.post.service.port.PostRepository;
import com.example.demo.user.domain.User;
import com.example.demo.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

  private final PostRepository postRepository;
  private final UserService userService;
  private final ClockHolder clockHolder;

  public Post getById(long id) {
    return postRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Posts", id));
  }

  public Post create(PostCreate postCreate) {
    User writer = userService.getById(postCreate.getWriterId());
    Post post = Post.create(writer, postCreate, clockHolder);
    post = postRepository.save(post);
    return post;
  }

  public Post update(long id, PostUpdate postUpdate) {
    Post post = getById(id);
    post = post.update(postUpdate, clockHolder);
    post = postRepository.save(post);
    return post;
  }
}
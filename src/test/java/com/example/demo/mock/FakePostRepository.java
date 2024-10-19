package com.example.demo.mock;

import com.example.demo.post.domain.Post;
import com.example.demo.post.service.port.PostRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class FakePostRepository implements PostRepository {

  private final AtomicLong generatedId = new AtomicLong(0);
  private final List<Post> posts = new ArrayList<>();

  @Override
  public Optional<Post> findById(long id) {
    return posts.stream().filter(post -> post.getId().equals(id)).findFirst();
  }

  @Override
  public Post save(Post post) {
    if (post.getId() == null || post.getId() == 0) {
      post = Post.builder()
          .id(generatedId.incrementAndGet())
          .content(post.getContent())
          .createdAt(post.getCreatedAt())
          .modifiedAt(post.getModifiedAt())
          .writer(post.getWriter())
          .build();
    } else {
      Post finalPost = post;
      posts.removeIf(data -> data.getId().equals(finalPost.getId()));
    }
    posts.add(post);
    return post;
  }
}


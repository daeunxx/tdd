package com.example.demo.mock;

import com.example.demo.common.service.port.ClockHolder;
import com.example.demo.common.service.port.UuidHolder;
import com.example.demo.post.controller.PostController;
import com.example.demo.post.controller.PostCreateController;
import com.example.demo.post.controller.port.PostService;
import com.example.demo.post.service.PostServiceImpl;
import com.example.demo.post.service.port.PostRepository;
import com.example.demo.user.controller.MyInfoController;
import com.example.demo.user.controller.UserController;
import com.example.demo.user.controller.UserCreateController;
import com.example.demo.user.controller.port.UserService;
import com.example.demo.user.service.CertificationService;
import com.example.demo.user.service.UserServiceImpl;
import com.example.demo.user.service.port.MailSender;
import com.example.demo.user.service.port.UserRepository;
import lombok.Builder;

public class TestContainer {

  public final MailSender mailSender;
  public final PostRepository postRepository;
  public final UserRepository userRepository;

  public final CertificationService certificationService;
  public final PostService postService;
  public final UserService userService;

  public final UserController userController;
  public final UserCreateController userCreateController;
  public final MyInfoController myInfoController;
  public final PostController postController;
  public final PostCreateController postCreateController;

  @Builder
  public TestContainer(UuidHolder uuidHolder, ClockHolder clockHolder) {
    this.mailSender = new FakeMailSender();
    this.postRepository = new FakePostRepository();
    this.userRepository = new FakeUserRepository();

    this.certificationService = new CertificationService(mailSender);
    this.postService = PostServiceImpl.builder()
        .postRepository(postRepository)
        .userRepository(userRepository)
        .clockHolder(clockHolder)
        .build();
    this.userService = UserServiceImpl.builder()
        .userRepository(userRepository)
        .certificationService(certificationService)
        .uuidHolder(uuidHolder)
        .clockHolder(clockHolder)
        .build();

    this.userController = UserController.builder()
        .userService(userService)
        .build();
    this.userCreateController = UserCreateController.builder()
        .userService(userService)
        .build();
    this.myInfoController = MyInfoController.builder()
        .userService(userService)
        .build();
    this.postController = PostController.builder()
        .postService(postService)
        .build();
    this.postCreateController = PostCreateController.builder()
        .postService(postService)
        .build();
  }
}

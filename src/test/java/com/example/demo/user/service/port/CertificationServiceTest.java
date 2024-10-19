package com.example.demo.user.service.port;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.demo.mock.FakeMailSender;
import com.example.demo.user.service.CertificationService;
import org.junit.jupiter.api.Test;

class CertificationServiceTest {

  @Test
  void 이메일_전송() {
    // given
    FakeMailSender fakeMailSender = new FakeMailSender();
    CertificationService certificationService = new CertificationService(fakeMailSender);

    // when
    certificationService.sendCertificationEmail(1, "sss3598@gmail.com",
        "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

    // then
    assertThat(fakeMailSender.email).isEqualTo("sss3598@gmail.com");
    assertThat(fakeMailSender.title).isEqualTo("Please certify your email address");
    assertThat(fakeMailSender.content).isEqualTo(
        "Please click the following link to certify your email address: http://localhost:8080/api/users/1/verify?certificationCode=aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
  }
}
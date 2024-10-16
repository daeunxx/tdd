package com.example.demo.user.service.port;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CertificationService {

  private final MailSender mailSender;

  public void sendCertificationEmail(long userId, String email, String certificationCode) {
    String certificationUrl = generateCertificationUrl(userId, certificationCode);
    String title = "Please certify your email address";
    String content = "Please click the following link to certify your email address: " + certificationUrl;

    mailSender.send(email, title, content);
  }

  private String generateCertificationUrl(long userId, String certificationCode) {
    return "http://localhost:8080/api/users/" + userId + "/verify?certificationCode=" + certificationCode;
  }

}

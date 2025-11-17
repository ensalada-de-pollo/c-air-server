package com.example.cnuairline.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.File;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

  private final JavaMailSender mailSender;

  public void sendPdfWithEmail(String to, String subject, String body, String fileName)
    throws MessagingException {
    MimeMessage mimeMessage = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

    helper.setTo(to);
    helper.setSubject(subject);
    helper.setText(body);

    FileSystemResource file = new FileSystemResource(new File(fileName));
    helper.addAttachment(file.getFilename(), file);

    mailSender.send(mimeMessage);
  }
}

package com.example.cnuairline.common;

import java.util.Properties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {

  @Value("${spring.mail.host}")
  private String mailServerHost;
  @Value("${spring.mail.port}")
  private int mailServerPort;
  @Value("${spring.mail.username}")
  private String mailServerUsername;
  @Value("${spring.mail.password}")
  private String mailServerPassword;

  @Bean
  public JavaMailSender javaMailSender() {
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

    mailSender.setHost(mailServerHost);
    mailSender.setPort(mailServerPort);

    mailSender.setUsername(mailServerUsername);
    mailSender.setPassword(mailServerPassword);

    Properties props = mailSender.getJavaMailProperties();
    props.put("mail.transport.protocol", "smtp");
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");

    return mailSender;
  }
}

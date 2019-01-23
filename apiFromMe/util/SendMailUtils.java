package org.leadingsoft.golf.api.util;

import org.leadingsoft.golf.api.code.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

@Component
public class SendMailUtils {

  @Autowired
  JavaMailSenderImpl mailSender;
  @Value("${mail.fromMail.addr}")
  private String sendFromMail;

  public String sendMail(String sendToMail, String replyTo, String sendSubject,
      String sendMessage) {
    SimpleMailMessage mailMessage = new SimpleMailMessage();
    mailMessage.setTo(sendToMail);
    if (StringUtils.isNotEmpty(replyTo)) {
      // リプライのメールアドレス
      mailMessage.setReplyTo(replyTo);
    }
    // Fromのメールアドレス
    mailMessage.setFrom(sendFromMail);
    // タイトル
    mailMessage.setSubject(sendSubject);
    // 本文送信
    mailMessage.setText(sendMessage);
    try {
      mailSender.send(mailMessage);
      return ResultCode.OK.code();
    } catch (Exception e) {
      System.out.println(e);
      return ResultCode.NG.code();
    }
  }
}

package com.starfish.mail.service;

import com.starfish.mail.enums.EmailTypeEnum;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Properties;

/**
 * 邮件发送服务
 *
 * @author sunkolin
 * @version 1.0.0
 * @since 2026-04-24
 */
@Slf4j
@Service
public class EmailService {

    /**
     * 发送邮件
     *
     * @param senderEmail   发件人邮箱
     * @param password      密码/授权码
     * @param receiverEmail 收件人邮箱
     * @param subject       邮件标题
     * @param content       邮件内容
     * @return 是否发送成功
     */
    public boolean sendEmail(String senderEmail, String password, String receiverEmail, String subject, String content) {
        try {
            // 获取邮箱类型
            EmailTypeEnum emailType = EmailTypeEnum.getByEmail(senderEmail);
            if (emailType == null) {
                log.error("不支持的邮箱类型: {}", senderEmail);
                return false;
            }

            // 配置SMTP服务器
            Properties props = new Properties();
            props.put("mail.smtp.host", emailType.getSmtpHost());
            props.put("mail.smtp.port", emailType.getSmtpPort().toString());
            props.put("mail.smtp.auth", "true");

            // 根据协议配置SSL/TLS
            if ("SSL".equals(emailType.getProtocol())) {
                props.put("mail.smtp.ssl.enable", "true");
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.put("mail.smtp.socketFactory.port", emailType.getSmtpPort().toString());
            } else if ("TLS".equals(emailType.getProtocol())) {
                props.put("mail.smtp.starttls.enable", "true");
            }

            // 创建会话
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(senderEmail, password);
                }
            });

            session.setDebug(false);

            // 创建邮件消息
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiverEmail));
            message.setSubject(subject, "UTF-8");
            message.setText(content, "UTF-8");

            // 发送邮件
            Transport.send(message);

            log.info("邮件发送成功: {} -> {}", senderEmail, receiverEmail);
            return true;

        } catch (Exception e) {
            log.error("邮件发送失败: {} -> {}", senderEmail, receiverEmail, e);
            return false;
        }
    }
}

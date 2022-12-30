package cn.xueden.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * @author:梁志杰
 * @date:2022/12/24
 * @description:cn.xueden.email
 * @version:1.0
 */
@Component
public class MailService {

    @Autowired
    JavaMailSender javaMailSender;

    /**
     *
     * @param from
     *        邮件发送者
     * @param to
     *        收件人
     * @param cc
     *        抄送人
     * @param subject
     *        邮件主题
     * @param content
     *        邮件内容
     */
    public void sendSimpleMail(String from, String to, String cc, String subject, String content) {
        // 简单邮件直接构建一个 SimpleMailMessage 对象进行配置并发送即可
        SimpleMailMessage simpMsg = new SimpleMailMessage();
        simpMsg.setFrom(from);
        simpMsg.setTo(to);
        simpMsg.setCc(cc);
        simpMsg.setSubject(subject);
        simpMsg.setText(content);
        javaMailSender.send(simpMsg);
    }


}

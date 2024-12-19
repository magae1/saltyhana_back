package com.saltyhana.saltyhanaserver.service;

import com.saltyhana.saltyhanaserver.dto.MailDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;
    private static final String title = "임시 비밀번호 안내 이메일입니다.";
    private static final String message = "안녕하세요. 임시 비밀번호 안내 메일입니다. "
            + "\n" + "회원님의 임시 비밀번호는 아래와 같습니다. 로그인 후 반드시 비밀번호를 변경해주세요." + "\n";

    @Value("${spring.mail.username}")
    private String from;

    public MailDTO createMail(String tmpPassword, String to) {
        MailDTO mailDto = new MailDTO(from, to, title, message + tmpPassword);
        return mailDto;
    }

    public void sendMail(MailDTO mailDto) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(mailDto.getTo());
        mailMessage.setSubject(mailDto.getTitle());
        mailMessage.setText(mailDto.getMessage());
        mailMessage.setFrom(mailDto.getFrom());
        mailMessage.setReplyTo(mailDto.getFrom());

        mailSender.send(mailMessage);
    }
}
package com.azat4dev.booking.users.users_commands.data.services.email;

import com.azat4dev.booking.users.users_commands.domain.core.values.email.EmailAddress;
import com.azat4dev.booking.users.users_commands.domain.interfaces.services.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.io.UnsupportedEncodingException;

@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final String host;
    private final int protocol;

    @Override
    public void send(
        EmailAddress toEmail,
        EmailData data
    ) {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();

        sender.setHost(host);
        sender.setProtocol("smtp");
        sender.setPort(protocol);

        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setTo(toEmail.getValue());
            helper.setFrom(data.from().getValue(), data.fromName());
            helper.setSubject(data.subject());
            helper.setText(data.body().value());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        sender.send(message);
    }
}
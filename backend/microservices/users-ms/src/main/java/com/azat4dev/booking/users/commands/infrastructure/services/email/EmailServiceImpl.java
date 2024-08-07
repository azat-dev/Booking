package com.azat4dev.booking.users.commands.infrastructure.services.email;

import com.azat4dev.booking.users.commands.domain.core.values.email.EmailAddress;
import com.azat4dev.booking.users.commands.domain.interfaces.services.EmailService;
import io.micrometer.observation.annotation.Observed;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.io.UnsupportedEncodingException;

@Slf4j
@Observed
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
            log.atInfo().log("Email sent");
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.atError()
                .setCause(e)
                .log("Failed to send email");
            throw new RuntimeException(e);
        }

        sender.send(message);
    }
}
package com.azat4dev.demobooking.users.domain.policies;

import com.azat4dev.demobooking.common.CommandId;
import com.azat4dev.demobooking.users.domain.events.UserCreated;
import com.azat4dev.demobooking.users.domain.events.UserCreatedPayload;
import com.azat4dev.demobooking.users.domain.services.EmailVerificationStatus;
import com.azat4dev.demobooking.users.domain.services.User;
import com.azat4dev.demobooking.users.domain.values.EmailAddress;
import com.azat4dev.demobooking.users.domain.values.UserId;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.util.Date;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

interface SendVerificationEmailPolicy {
    void execute(UserCreated event);
}

class EmailBody {
    private final String value;

    public EmailBody(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EmailBody emailBody)) return false;
        return Objects.equals(getValue(), emailBody.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getValue());
    }
}

class VerificationEmailBuilderResult {
    private final String subject;
    private final EmailBody body;

    public VerificationEmailBuilderResult(String subject, EmailBody body) {
        if (subject == null) {
            throw new IllegalArgumentException("subject");
        }

        if (body == null) {
            throw new IllegalArgumentException("body");
        }

        this.subject = subject;
        this.body = body;
    }

    public EmailBody getBody() {
        return body;
    }

    public String getSubject() {
        return subject;
    }
}

interface VerificationEmailBuilder {
    VerificationEmailBuilderResult build(UserId userId);
}

class EmailData {
    private final String subject;
    private final EmailBody body;

    public EmailData(String subject, EmailBody body) {
        this.subject = subject;
        this.body = body;
    }

    public String getSubject() {
        return subject;
    }

    public EmailBody getBody() {
        return body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EmailData emailData)) return false;
        return Objects.equals(getSubject(), emailData.getSubject()) && Objects.equals(getBody(), emailData.getBody());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSubject(), getBody());
    }
}

interface EmailService {
    void send(EmailAddress email, EmailData data);
}

class SendVerificationEmailPolicyImpl implements SendVerificationEmailPolicy {

    private VerificationEmailBuilder emailBuilder;
    private EmailService emailService;

    public SendVerificationEmailPolicyImpl(
        EmailService emailService,
        VerificationEmailBuilder emailBuilder
    ) {
        this.emailService = emailService;
        this.emailBuilder = emailBuilder;
    }

    @Override
    public void execute(UserCreated event) {

        final var user = event.getPayload().getUser();
        final var userId = user.getId();

        final var builtData = emailBuilder.build(userId);

        emailService.send(
            user.getEmail(),
            new EmailData(
                builtData.getSubject(),
                builtData.getBody()
            )
        );
    }
}

public class SendVerificationEmailPolicyTests {

    record SUT(
        SendVerificationEmailPolicy policy,
        EmailService emailService,
        VerificationEmailBuilder emailBuilder
    ) {}

    SUT createSUT() {

        final var emailBuilder = mock(VerificationEmailBuilder.class);
        final var emailService = mock(EmailService.class);

        return new SUT(
            new SendVerificationEmailPolicyImpl(
                emailService,
                emailBuilder
            ),
            emailService,
            emailBuilder
        );
    }

    EmailAddress anyValidEmail() {
        try {
            return EmailAddress.makeFromString("user@email.com");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void given_valid_user__when_SendVerificationEmailPolicyImpl_triggered__then_build_email_and_send() {

        // Given
        final var sut = createSUT();
        final var email = anyValidEmail();
        final var userId = UserId.generateNew();

        final var event = new UserCreated(
                CommandId.generateNew(),
                Clock.systemUTC().millis(),
                new UserCreatedPayload(
                        new User(
                            userId,
                            email,
                            EmailVerificationStatus.NOT_VERIFIED,
                            new Date()
                        )
                )
        );

        final var builtEmail = new VerificationEmailBuilderResult(
            "subject",
            new EmailBody("body")
        );

        given(sut.emailBuilder.build(any()))
            .willReturn(builtEmail);

        willDoNothing()
            .given(sut.emailService).send(any(), any());


        // When
        sut.policy.execute(event);

        // Then
        then(sut.emailBuilder)
            .should(times(1))
            .build(userId);

        then(sut.emailService)
            .should(times(1))
            .send(
                eq(email),
                assertArg(m -> {
                    assertThat(m.getSubject()).isEqualTo(builtEmail.getSubject());
                    assertThat(m.getBody()).isEqualTo(builtEmail.getBody());
                })
            );
    }
}

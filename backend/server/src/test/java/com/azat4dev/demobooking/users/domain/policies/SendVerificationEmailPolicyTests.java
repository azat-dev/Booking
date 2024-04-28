package com.azat4dev.demobooking.users.domain.policies;

import com.azat4dev.demobooking.common.CommandId;
import com.azat4dev.demobooking.users.domain.events.UserCreated;
import com.azat4dev.demobooking.users.domain.events.UserCreatedPayload;
import com.azat4dev.demobooking.users.domain.services.EmailService;
import com.azat4dev.demobooking.users.domain.services.EmailVerificationStatus;
import com.azat4dev.demobooking.users.domain.entities.User;
import com.azat4dev.demobooking.users.domain.services.VerificationEmailBuilder;
import com.azat4dev.demobooking.users.domain.services.VerificationEmailBuilderResult;
import com.azat4dev.demobooking.users.domain.values.EmailAddress;
import com.azat4dev.demobooking.users.domain.values.UserId;
import com.azat4dev.demobooking.users.domain.values.email.EmailBody;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;


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
                    assertThat(m.subject()).isEqualTo(builtEmail.subject());
                    assertThat(m.body()).isEqualTo(builtEmail.body());
                })
            );
    }
}

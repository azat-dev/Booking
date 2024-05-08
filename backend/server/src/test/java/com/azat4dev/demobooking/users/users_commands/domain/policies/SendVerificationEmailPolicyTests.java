package com.azat4dev.demobooking.users.users_commands.domain.policies;

import com.azat4dev.demobooking.common.CommandId;
import com.azat4dev.demobooking.users.users_commands.domain.UserHelpers;
import com.azat4dev.demobooking.users.users_commands.domain.events.UserCreated;
import com.azat4dev.demobooking.users.users_commands.domain.events.UserCreatedPayload;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.services.EmailService;
import com.azat4dev.demobooking.users.users_commands.domain.services.EmailVerificationStatus;
import com.azat4dev.demobooking.users.users_commands.domain.services.VerificationEmailBuilder;
import com.azat4dev.demobooking.users.users_commands.domain.services.VerificationEmailBuilderResult;
import com.azat4dev.demobooking.users.users_commands.domain.values.email.EmailAddress;
import com.azat4dev.demobooking.users.users_commands.domain.values.email.EmailBody;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;


public class SendVerificationEmailPolicyTests {

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
        final var userId = UserHelpers.anyValidUserId();

        final var event = new UserCreated(
            CommandId.generateNew(),
            Clock.systemUTC().millis(),
            new UserCreatedPayload(
                LocalDateTime.now(),
                userId,
                UserHelpers.anyFullName(),
                email,
                EmailVerificationStatus.NOT_VERIFIED
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

    record SUT(
        SendVerificationEmailPolicy policy,
        EmailService emailService,
        VerificationEmailBuilder emailBuilder
    ) {
    }
}

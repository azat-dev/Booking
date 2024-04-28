package com.azat4dev.demobooking.users.data.services;

import com.azat4dev.demobooking.users.domain.services.*;
import com.azat4dev.demobooking.users.domain.values.UserId;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class VerificationTokensServiceImpl implements VerificationTokensService {

    @Override
    public VerificationToken makeVerificationToken(UserId userId, Date expirationDate) {
        return null;
    }

    @Override
    public UserId parse(VerificationToken token) throws ExpiredVerificationToken, WrongFormatOfVerificationToken {
        return null;
    }
}

public class VerificationTokensServiceImplTests {

    record SUT (
        VerificationTokensService service
    ) {}

    SUT createSUT() {
        return new SUT(
            new VerificationTokensServiceImpl()
        );
    }

    @Test
    void test_given_valid_data__when_makeVerificationToken_and_parse__then_tokens_should_match() throws ExpiredVerificationToken, WrongFormatOfVerificationToken {

        // Given
        final var sut = createSUT();
        final var userId = UserId.generateNew();
        final var expirationDate = new Date();

        // When
        final var token = sut.service.makeVerificationToken(userId, expirationDate);

        // Then
        assertThat(token).isNotNull();

        // When
        final var parsedUserId = sut.service.parse(token);

        // Then
        assertThat(parsedUserId).isEqualTo(userId);
    }
}

package com.azat4dev.demobooking.users.data.repositories.jpa;


import com.azat4dev.demobooking.users.data.entities.UserData;
import com.azat4dev.demobooking.users.domain.UserHelpers;
import com.azat4dev.demobooking.users.domain.services.EmailVerificationStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class JpaUsersRepositoryTests {

    @Autowired
    private JpaUsersRepository jpaUsersRepository;

    String anyEmail() {
        return "example@email.com";
    }

    @Test
    void test_findByEmail_givenEmptyDb_thenReturnEmpty() {

        // Given
        final var email = anyEmail();

        // When
        final var result = jpaUsersRepository.findByEmail(email);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void test_findByEmail_givenValidEmail_thenReturnUser() {

        // Given
        final var existingUser1 = givenExistingUser();
        final var existingUser2 = givenExistingUser();


        // When
        final var result1 = jpaUsersRepository.findByEmail(existingUser1.getEmail());
        final var result2 = jpaUsersRepository.findByEmail(existingUser2.getEmail());

        // Then
        assertThat(result1.get()).isEqualTo(existingUser1);
        assertThat(result2.get()).isEqualTo(existingUser2);
    }

    UserData givenExistingUser() {

        final var user = UserHelpers.anyUser();

        final var userData = new UserData(
            user.id().value(),
            LocalDateTime.now(),
            LocalDateTime.now(),
            user.email().getValue(),
            user.fullName().firstName().getValue(),
            user.fullName().lastName().getValue(),
            user.encodedPassword().value(),
            EmailVerificationStatus.NOT_VERIFIED
        );

        return jpaUsersRepository.saveAndFlush(userData);
    }
}

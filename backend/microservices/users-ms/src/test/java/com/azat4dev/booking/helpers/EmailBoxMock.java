package com.azat4dev.booking.helpers;


import com.azat4dev.booking.users.commands.domain.core.values.email.EmailAddress;
import com.azat4dev.booking.users.commands.domain.interfaces.services.EmailService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;

public class EmailBoxMock {

    private final List<EmailBoxItem> items = new ArrayList<>();

    public synchronized void add(EmailAddress email, EmailService.EmailData body) {
        items.add(new EmailBoxItem(LocalDateTime.now(), email, body));
    }

    public synchronized EmailBoxItem last() {
        return items.get(items.size() - 1);
    }

    public synchronized void clear() {
        items.clear();
    }

    public void clearFor(EmailAddress email) {
        items.removeIf(item -> item.email().equals(email));
    }

    public synchronized Optional<EmailBoxItem> lastFor(Predicate<EmailBoxItem> predicate) {
        for (int i = items.size() - 1; i >= 0; i--) {

            final var item = items.get(i);

            if (predicate.test(item)) {
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }

    public Optional<EmailBoxItem> waitFor(int seconds, Predicate<EmailBoxItem> predicate) {
        await()
            .pollInterval(Duration.ofMillis(200))
            .atMost(seconds, SECONDS)
            .until(() -> {
                return lastFor(predicate).isPresent();
            });

        return lastFor(predicate);
    }

    public record EmailBoxItem(
        LocalDateTime capturedAt,
        EmailAddress email,
        EmailService.EmailData data
    ) {
    }
}
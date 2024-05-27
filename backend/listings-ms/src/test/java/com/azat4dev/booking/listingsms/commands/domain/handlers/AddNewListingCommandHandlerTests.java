package com.azat4dev.booking.listingsms.commands.domain.handlers;

import com.azat4dev.booking.shared.domain.DomainException;
import com.azat4dev.booking.listingsms.commands.domain.commands.AddNewListing;
import com.azat4dev.booking.listingsms.commands.domain.interfaces.repositories.ListingsRepository;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingTitle;
import com.azat4dev.booking.listingsms.commands.domain.values.MakeNewListingId;
import com.azat4dev.booking.listingsms.commands.domain.values.OwnerId;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;

public class AddNewListingCommandHandlerTests {

    record SUT(
        AddNewListingCommandHandler handler,
        ListingsRepository repository,
        MakeNewListingId makeListingId
    ) {}

    SUT createSUT() {
        var repository = mock(ListingsRepository.class);
        var makeListingId = mock(MakeNewListingId.class);
        var handler = new AddNewListingCommandHandler(repository, makeListingId);

        return new SUT(handler, repository, makeListingId);
    }

    ListingId anyListingId() {
        return ListingId.dangerouslyMakeFrom(UUID.randomUUID().toString());
    }

    OwnerId anyOwnerId() {
        return OwnerId.dangerouslyMakeFrom(UUID.randomUUID().toString());
    }

    ListingTitle anyTitle() {
        return ListingTitle.dangerouslyMakeFrom("title");
    }

    @Test
    void test_handle_givenAddNewListingCommand_thenAddNewListingToRepositoryAndReturnId() throws DomainException {

        // Given
        var sut = createSUT();
        var command = new AddNewListing(
            anyListingId(),
            anyOwnerId(),
            ListingTitle.dangerouslyMakeFrom("title")
        );

        given(sut.makeListingId.make())
            .willReturn(command.getListingId());

        willDoNothing().given(sut.repository)
            .addNew(any());

        // When
        var result = sut.handler.handle(command);

        then(sut.repository).should(times(1))
            .addNew(any());

        assertThat(result).isEqualTo(command.getListingId());
    }
}

package com.azat4dev.booking.listingsms.commands.domain.entities;

import com.azat4dev.booking.listingsms.commands.domain.values.HostId;
import io.micrometer.observation.annotation.Observed;
import lombok.AllArgsConstructor;


@Observed
@AllArgsConstructor
public class HostsImpl implements Hosts {

    private final HostListingsFactory hostListingsFactory;

    @Override
    public Host getById(HostId hostId) {
        return new HostImpl(
            hostId,
            hostListingsFactory
        );
    }

    @AllArgsConstructor
    public static class HostImpl implements Host {

        private final HostId hostId;
        private final HostListingsFactory hostListingsFactory;

        @Override
        public HostId getId() {
            return hostId;
        }

        @Override
        public HostListings getListings() {
            return this.hostListingsFactory.make(hostId);
        }
    }
}

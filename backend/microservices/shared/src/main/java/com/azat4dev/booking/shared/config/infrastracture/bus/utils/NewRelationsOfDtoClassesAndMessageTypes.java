package com.azat4dev.booking.shared.config.infrastracture.bus.utils;

import lombok.Getter;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@Getter
public final class NewRelationsOfDtoClassesAndMessageTypes {

    private final List<RelationsOfDtoClassesAndMessageTypes.Item> items = new LinkedList<>();

    public NewRelationsOfDtoClassesAndMessageTypes(
        RelationsOfDtoClassesAndMessageTypes.Item... items
    ) {
        this.items.addAll(Arrays.asList(items));
    }

    public NewRelationsOfDtoClassesAndMessageTypes(
        Collection<RelationsOfDtoClassesAndMessageTypes.Item> items
    ) {
        this.items.addAll(items);
    }

    public void add(RelationsOfDtoClassesAndMessageTypes.Item item) {
        items.add(item);
    }

    public void addAll(Collection<RelationsOfDtoClassesAndMessageTypes.Item> items) {
        this.items.addAll(items);
    }
}

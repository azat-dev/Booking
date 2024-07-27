package com.azat4dev.booking.shared.config.infrastracture.bus.utils;

import lombok.Getter;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@Getter
public final class ItemsToAddInOneToOneRelationsOfDtoClassesAndMessageTypes {

    private final List<OneToOneRelationsOfDtoClassesAndMessageTypes.Item> items = new LinkedList<>();

    public ItemsToAddInOneToOneRelationsOfDtoClassesAndMessageTypes(
        OneToOneRelationsOfDtoClassesAndMessageTypes.Item... items
    ) {
        this.items.addAll(Arrays.asList(items));
    }

    public ItemsToAddInOneToOneRelationsOfDtoClassesAndMessageTypes(
        Collection<OneToOneRelationsOfDtoClassesAndMessageTypes.Item> items
    ) {
        this.items.addAll(items);
    }

    public void add(OneToOneRelationsOfDtoClassesAndMessageTypes.Item item) {
        items.add(item);
    }

    public void addAll(Collection<OneToOneRelationsOfDtoClassesAndMessageTypes.Item> items) {
        this.items.addAll(items);
    }
}

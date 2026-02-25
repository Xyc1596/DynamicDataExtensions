package com.xyc.dynamicdataext.base;

import com.google.common.collect.Sets;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.LinkedHashSet;
import java.util.Set;

public class EntryAndTagCollection<T> {
    protected final Set<T> entries = new LinkedHashSet<>();
    protected final Set<TagKey<T>> tags = new LinkedHashSet<>();
    protected final Set<T> allEntries = new LinkedHashSet<>();
    protected final Registry<T> registry;

    protected EntryAndTagCollection(Registry<T> registry) {
        this.registry = registry;
    }

    public static EntryAndTagCollection<Item> items() {
        return new EntryAndTagCollection<>(BuiltInRegistries.ITEM);
    }

    public EntryAndTagCollection<T> parseStrings(Iterable<String> ids) {
        for (String id : ids) {
            if (id.startsWith("#")) {
                ResourceLocation location = ResourceLocation.tryParse(id.substring(1));
                if (location != null)
                    this.addTag(TagKey.create(this.registry.key(), location));
            } else {
                ResourceLocation location = ResourceLocation.tryParse(id);
                if (location != null && this.registry.containsKey(location))
                    this.addEntry(this.registry.get(location));
            }
        }
        return this;
    }

    public void addEntry(T entry) {
        this.entries.add(entry);
        this.allEntries.add(entry);
    }

    @SuppressWarnings("unused")
    public void addEntries(Iterable<T> entries) {
        for (T e : entries)
            this.addEntry(e);
    }

    public void addTag(TagKey<T> tag) {
        this.tags.add(tag);
        this.registry.getTag(tag).ifPresent(
            holders -> holders.forEach(holder -> this.allEntries.add(holder.value()))
        );
    }

    @SuppressWarnings("unused")
    public boolean contains(T entry) {
        return this.allEntries.contains(entry);
    }

    public boolean isEmpty() {
        return this.allEntries.isEmpty();
    }

    public Set<T> applyToForSet(Set<T> entries, boolean whitelist) {
        if (entries.isEmpty())
            return Set.of();
        return this.isEmpty() ^ whitelist
            ? Sets.intersection(entries, this.allEntries)
            : Sets.difference(entries, this.allEntries);
    }
}

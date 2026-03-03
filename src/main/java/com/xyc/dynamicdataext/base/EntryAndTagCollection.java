package com.xyc.dynamicdataext.base;

import com.google.common.collect.Sets;
import com.mojang.logging.LogUtils;
import com.xyc.dynamicdataext.utils.RegistryUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.slf4j.Logger;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class EntryAndTagCollection<T> {
    public static final Logger LOGGER = LogUtils.getLogger();

    protected final Set<T> entries = new LinkedHashSet<>();
    protected final Set<TagKey<T>> tags = new LinkedHashSet<>();
    protected final Set<T> allEntries = new LinkedHashSet<>();
    protected final Registry<T> registry;

    protected EntryAndTagCollection(Registry<T> registry) {
        this.registry = registry;
    }

    public static EntryAndTagCollection<Item> items() {
        return new EntryAndTagCollection<>(RegistryUtils.ITEM_REGISTRY);
    }

    public EntryAndTagCollection<T> parseStrings(Iterable<String> ids) {
        Set<Pattern> entryPatterns = new LinkedHashSet<>();
        Set<Pattern> tagPatterns = new LinkedHashSet<>();
        for (String id : ids) {
            if (id.startsWith("@")) {
                String id_ = id.substring(1);
                try {
                    if (id_.startsWith("#"))
                        tagPatterns.add(Pattern.compile(id_.substring(1)));
                    else
                        entryPatterns.add(Pattern.compile(id_));
                } catch (PatternSyntaxException e) {
                    LOGGER.warn(e.getLocalizedMessage());
                }
            } else
                this.addString(id);
        }

        if (!entryPatterns.isEmpty()) {
            for (Holder<T> holder : registry.asHolderIdMap()) {
                String id = holder.getRegisteredName();
                for (Pattern pattern : entryPatterns)
                    if (pattern.matcher(id).matches())
                        this.addEntry(id);
            }
        }
        if (!tagPatterns.isEmpty()) {
            registry.getTagNames().forEach(tag -> {
                String id = tag.location().toString();
                for (Pattern pattern : tagPatterns)
                    if (pattern.matcher(id).matches())
                        this.addTag(id);
            });
        }
        return this;
    }

    public void addString(String id) {
        if (id.startsWith("#"))
            this.addTag(id.substring(1));
        else
            this.addEntry(id);
    }

    public void addEntry(String id) {
        ResourceLocation location = ResourceLocation.tryParse(id);
        if (location != null && this.registry.containsKey(location))
            this.addEntry(this.registry.get(location));
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

    public void addTag(String id) {
        ResourceLocation location = ResourceLocation.tryParse(id);
        if (location != null)
            this.addTag(TagKey.create(this.registry.key(), location));
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

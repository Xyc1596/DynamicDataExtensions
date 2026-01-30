package com.xyc.practicalextensions.base;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.tags.TagKey;

import java.util.*;

public interface IInjectingTags {
    void practicalextensions$injectTags();

    private static <T> void injectTagsIntoRegistryTyped(
        RegistryAccess.RegistryEntry<T> registryEntry,
        Map<TagKey<T>, Set<Holder<T>>> tagsToAdd,
        Map<TagKey<T>, Set<TagKey<T>>> tagsToRemove
    ) {
        Registry<T> registry = registryEntry.value();
        Map<TagKey<T>, List<Holder<T>>> newTags = new HashMap<>();
        registry.getTags().forEach(
            entry -> {
                TagKey<T> key = entry.getFirst();
                boolean unmodified = true;
                if (tagsToRemove.containsKey(key)) {
                    Set<TagKey<T>> toRemove = tagsToRemove.get(key);
                    newTags.put(
                        key,
                        toRemove.isEmpty() ? new ArrayList<>() : entry.getSecond().stream().toList()
                    );
                    unmodified = false;
                }
                if (tagsToAdd.containsKey(key)) {
                    Set<Holder<T>> toAdd = tagsToAdd.get(key);
                    newTags.putIfAbsent(key, new ArrayList<>());
                    newTags.get(key).addAll(toAdd);
                    unmodified = false;
                }
                if (unmodified) {
                    newTags.put(key, entry.getSecond().stream().toList());
                }
            }
        );
        registry.bindTags(newTags);
    }

    @SuppressWarnings("unchecked")
    static <T> void injectTagsIntoRegistry(
        RegistryAccess.RegistryEntry<?> registryEntry,
        Map<TagKey<?>, Set<Holder<?>>> tagsToAdd,
        Map<TagKey<?>, Set<TagKey<?>>> tagsToRemove
    ) {
        injectTagsIntoRegistryTyped(
            (RegistryAccess.RegistryEntry<T>) registryEntry,
            (Map<TagKey<T>, Set<Holder<T>>>) (Map<?, ?>) tagsToAdd,
            (Map<TagKey<T>, Set<TagKey<T>>>) (Map<?, ?>) tagsToRemove
        );
    }
}

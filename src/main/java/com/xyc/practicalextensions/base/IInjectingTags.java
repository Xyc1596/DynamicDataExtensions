package com.xyc.practicalextensions.base;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

import java.util.*;
import java.util.stream.Collectors;

public interface IInjectingTags {
    int[] practicalextensions$injectTags();

    private static <T> int[] injectTagsIntoRegistryTyped(
        RegistryAccess.RegistryEntry<T> registryEntry,
        Map<TagKey<T>, Set<Holder<T>>> tagsToAdd,
        Map<TagKey<T>, Set<ResourceKey<T>>> tagsToRemove
    ) {
        Registry<T> registry = registryEntry.value();
        Map<TagKey<T>, List<Holder<T>>> newTags = new HashMap<>(
            registry.getTags().collect(Collectors.toMap(
                Pair::getFirst, p -> p.getSecond().stream().collect(Collectors.toCollection(ArrayList::new))
            ))
        );

        Set<TagKey<T>> keysRemoved = new HashSet<>(), keysModified = new HashSet<>();
        for (Map.Entry<TagKey<T>, Set<ResourceKey<T>>> entry : tagsToRemove.entrySet()) {
            TagKey<T> key = entry.getKey();
            if (newTags.containsKey(key)) {
                Set<ResourceKey<T>> toRemove = entry.getValue();
                if (toRemove.isEmpty()) {
                    keysRemoved.add(key);
                    newTags.remove(key);
                } else {
                    keysModified.add(key);
                    newTags.get(key).removeIf(h -> toRemove.contains(h.getKey()));
                }
            }
        }

        Set<TagKey<T>> keysAdded = tagsToAdd.keySet();
        for (Map.Entry<TagKey<T>, Set<Holder<T>>> entry : tagsToAdd.entrySet()) {
            TagKey<T> key = entry.getKey();
            if (newTags.containsKey(key)) {
                keysModified.add(key);
                List<Holder<T>> l = newTags.get(key);
                Set<Holder<T>> s = new HashSet<>(l);
                for (Holder<T> holder : entry.getValue()) {
                    if (s.add(holder)) {
                        l.addAll(entry.getValue());
                    }
                }
            } else {
                keysAdded.add(key);
                newTags.put(key, new ArrayList<>(entry.getValue()));
            }
        }

        registry.bindTags(newTags);

        Set<TagKey<T>> keysRemovedAndAdded = new HashSet<>(keysAdded);
        keysRemovedAndAdded.retainAll(keysRemoved);
        int nRemovedAndAdded = keysRemovedAndAdded.size();
        return new int[]{
            keysAdded.size() - nRemovedAndAdded,
            keysRemoved.size() - nRemovedAndAdded,
            keysModified.size() + nRemovedAndAdded
        };
    }

    @SuppressWarnings("unchecked")
    static <T> int[] injectTagsIntoRegistry(
        RegistryAccess.RegistryEntry<?> registryEntry,
        Map<TagKey<?>, Set<Holder<?>>> tagsToAdd,
        Map<TagKey<?>, Set<ResourceKey<?>>> tagsToRemove
    ) {
        return injectTagsIntoRegistryTyped(
            (RegistryAccess.RegistryEntry<T>) registryEntry,
            (Map<TagKey<T>, Set<Holder<T>>>) (Map<?, ?>) tagsToAdd,
            (Map<TagKey<T>, Set<ResourceKey<T>>>) (Map<?, ?>) tagsToRemove
        );
    }
}

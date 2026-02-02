package com.xyc.dynamicdataext.base;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.TagManager;

import java.util.*;
import java.util.stream.Collectors;

public interface IInjectingTags {
    int[] dynamicdataext$injectTags();

    private static <T> Pair<Map<ResourceLocation, Collection<Holder<T>>>, int[]> injectTagsByRegistryTyped(
        TagManager.LoadResult<T> result,
        Map<TagKey<T>, Set<Holder<T>>> tagsToAdd,
        Map<TagKey<T>, Set<Holder<T>>> tagsToRemove
    ) {
        Map<ResourceLocation, Set<Holder<T>>> tagsToAddInLocation = tagsToAdd
            .entrySet().stream()
            .filter(e -> !e.getValue().isEmpty())
            .map(e -> Map.entry(e.getKey().location(), e.getValue()))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        Map<ResourceLocation, Set<Holder<T>>> tagsToRemoveInLocation = tagsToRemove
            .entrySet().stream()
            .map(e -> Map.entry(e.getKey().location(), e.getValue()))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        Map<ResourceLocation, Collection<Holder<T>>> newTags = new HashMap<>();
        for (Map.Entry<ResourceLocation, Collection<Holder<T>>> entry : result.tags().entrySet()) {
            newTags.put(entry.getKey(), new LinkedHashSet<>(entry.getValue()));
        }

        Set<ResourceLocation> locationsRemoved = new HashSet<>(), locationsModified = new HashSet<>();
        for (Map.Entry<ResourceLocation, Set<Holder<T>>> entry : tagsToRemoveInLocation.entrySet()) {
            ResourceLocation location = entry.getKey();
            if (newTags.containsKey(location)) {
                Set<Holder<T>> items = entry.getValue();
                if (items.isEmpty()) {
                    locationsRemoved.add(location);
                    newTags.remove(location);
                } else {
                    locationsModified.add(location);
                    newTags.get(location).removeIf(items::contains);
                }
            }
        }

        Set<ResourceLocation> locationsAdded = new HashSet<>();
        for (Map.Entry<ResourceLocation, Set<Holder<T>>> entry : tagsToAddInLocation.entrySet()) {
            ResourceLocation location = entry.getKey();
            Set<Holder<T>> items = entry.getValue();
            if (newTags.containsKey(location)) {
                locationsModified.add(location);
                newTags.put(location, new LinkedHashSet<>(newTags.get(location)));
                newTags.get(location).addAll(items);
            } else {
                locationsAdded.add(location);
                newTags.put(location, items);
            }
        }

        Set<ResourceLocation> locationsRemovedAndAdded = new HashSet<>(locationsAdded);
        locationsRemovedAndAdded.retainAll(locationsRemoved);
        int nRemovedAndAdded = locationsRemovedAndAdded.size();
        return Pair.of(
            newTags.entrySet().stream().collect(Collectors.toUnmodifiableMap(
                Map.Entry::getKey,
                entry -> entry.getValue().stream().toList()
            )),
            new int[]{
                locationsAdded.size() - nRemovedAndAdded,
                locationsRemoved.size() - nRemovedAndAdded,
                locationsModified.size() + nRemovedAndAdded
            }
        );
    }

    @SuppressWarnings("unchecked")
    static <T> Pair<Map<ResourceLocation, Collection<Holder<?>>>, int[]> injectTagsByRegistry(
        TagManager.LoadResult<?> result,
        Map<TagKey<?>, Set<Holder<?>>> tagsToAdd,
        Map<TagKey<?>, Set<Holder<?>>> tagsToRemove
    ) {
        var newResult = injectTagsByRegistryTyped(
            (TagManager.LoadResult<T>) result,
            (Map<TagKey<T>, Set<Holder<T>>>) (Map<?, ?>) tagsToAdd,
            (Map<TagKey<T>, Set<Holder<T>>>) (Map<?, ?>) tagsToRemove
        );
        return Pair.of(
            (Map<ResourceLocation, Collection<Holder<?>>>) (Map<?, ?>) newResult.getFirst(),
            newResult.getSecond()
        );
    }

    @SuppressWarnings("unchecked")
    static <T> TagManager.LoadResult<?> createLoadResult(
        ResourceKey<? extends Registry<?>> key,
        Map<ResourceLocation, Collection<Holder<?>>> tags
    ) {
        return new TagManager.LoadResult<>(
            (ResourceKey<? extends Registry<T>>) key,
            (Map<ResourceLocation, Collection<Holder<T>>>) (Map<?, ?>) tags
        );
    }
}

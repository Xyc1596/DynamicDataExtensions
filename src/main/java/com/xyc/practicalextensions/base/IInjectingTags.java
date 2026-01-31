package com.xyc.practicalextensions.base;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.TagManager;

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

    private static <T> Pair<Map<ResourceLocation, Collection<Holder<T>>>, int[]> injectTagsIntoTagManagerTyped(
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
    static <T> Pair<Map<ResourceLocation, Collection<Holder<?>>>, int[]> injectTagsIntoTagManager(
        TagManager.LoadResult<?> result,
        Map<TagKey<?>, Set<Holder<?>>> tagsToAdd,
        Map<TagKey<?>, Set<Holder<?>>> tagsToRemove
    ) {
        var newResult = injectTagsIntoTagManagerTyped(
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
        return new TagManager.LoadResult<T>(
            (ResourceKey<? extends Registry<T>>) key,
            (Map<ResourceLocation, Collection<Holder<T>>>) (Map<?, ?>) tags
        );
    }
}

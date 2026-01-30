package com.xyc.practicalextensions.mixins.tags;

import com.xyc.practicalextensions.PracticalExtensionRegistry;
import com.xyc.practicalextensions.base.IInjectingTags;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.*;
import java.util.stream.Stream;

@Mixin(RegistryAccess.class)
public interface RegistryAccessMixin extends IInjectingTags {
    @Shadow Stream<RegistryAccess.RegistryEntry<?>> registries();

    @Unique
    @Override
    default void practicalextensions$injectTags() {
        var allTagsToUpdate = PracticalExtensionRegistry.getAllTagsToUpdate();

        Set<ResourceKey<? extends Registry<?>>> allResourceKeys = new HashSet<>();
        Map<ResourceKey<? extends Registry<?>>, Map<TagKey<?>, Set<Holder<?>>>> allTagsToAddInRegistry =
            new HashMap<>();
        Map<ResourceKey<? extends Registry<?>>, Map<TagKey<?>, Set<TagKey<?>>>> allTagsToRemoveInRegistry =
            new HashMap<>();

        for (Map.Entry<TagKey<?>, Set<Holder<?>>> entry : allTagsToUpdate.getLeft().entrySet()) {
            TagKey<?> key = entry.getKey();
            ResourceKey<? extends Registry<?>> registry = key.registry();
            allResourceKeys.add(registry);
            allTagsToAddInRegistry.putIfAbsent(registry, new HashMap<>());
            allTagsToAddInRegistry.get(registry).put(key, entry.getValue());
        }

        for (Map.Entry<TagKey<?>, Set<TagKey<?>>> entry : allTagsToUpdate.getRight().entrySet()) {
            TagKey<?> key = entry.getKey();
            ResourceKey<? extends Registry<?>> resourceKey = key.registry();
            allResourceKeys.add(resourceKey);
            allTagsToRemoveInRegistry.putIfAbsent(resourceKey, new HashMap<>());
            allTagsToRemoveInRegistry.get(resourceKey).put(key, entry.getValue());
        }

        this.registries()
            .filter(registryEntry -> allResourceKeys.contains(registryEntry.key()))
            .forEach(registryEntry -> {
                ResourceKey<? extends Registry<?>> resourceKey = registryEntry.key();
                IInjectingTags.injectTagsIntoRegistry(
                    registryEntry,
                    allTagsToAddInRegistry.getOrDefault(resourceKey, Map.of()),
                    allTagsToRemoveInRegistry.getOrDefault(resourceKey, Map.of())
                );
            });
    }
}

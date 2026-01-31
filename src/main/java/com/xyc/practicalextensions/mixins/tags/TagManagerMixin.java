package com.xyc.practicalextensions.mixins.tags;

import com.xyc.practicalextensions.PracticalExtensionRegistry;
import com.xyc.practicalextensions.base.IInjectingTags;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.TagManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.*;

@Mixin(TagManager.class)
public abstract class TagManagerMixin implements IInjectingTags {
    @Shadow
    private List<TagManager.LoadResult<?>> results;

    @Override
    public int[] practicalextensions$injectTags() {
        var allTagsToUpdate = PracticalExtensionRegistry.getAllTagsToUpdate();
        Set<ResourceKey<? extends Registry<?>>> allResourceKeys = new HashSet<>();
        Map<ResourceKey<? extends Registry<?>>, Map<TagKey<?>, Set<Holder<?>>>> allTagsToAddInRegistry =
            new HashMap<>();
        Map<ResourceKey<? extends Registry<?>>, Map<TagKey<?>, Set<Holder<?>>>> allTagsToRemoveInRegistry =
            new HashMap<>();

        for (Map.Entry<TagKey<?>, Set<Holder<?>>> entry : allTagsToUpdate.getLeft().entrySet()) {
            TagKey<?> key = entry.getKey();
            ResourceKey<? extends Registry<?>> registry = key.registry();
            allResourceKeys.add(registry);
            allTagsToAddInRegistry.putIfAbsent(registry, new HashMap<>());
            allTagsToAddInRegistry.get(registry).put(key, entry.getValue());
        }

        for (Map.Entry<TagKey<?>, Set<Holder<?>>> entry : allTagsToUpdate.getRight().entrySet()) {
            TagKey<?> key = entry.getKey();
            ResourceKey<? extends Registry<?>> resourceKey = key.registry();
            allResourceKeys.add(resourceKey);
            allTagsToRemoveInRegistry.putIfAbsent(resourceKey, new HashMap<>());
            allTagsToRemoveInRegistry.get(resourceKey).put(key, entry.getValue());
        }

        int[] i = {0, 0, 0};
        List<TagManager.LoadResult<?>> newResults = new ArrayList<>();
        for (TagManager.LoadResult<?> result : results) {
            ResourceKey<? extends Registry<?>> resourceKey = result.key();
            if (allResourceKeys.contains(resourceKey)) {
                var r = IInjectingTags.injectTagsIntoTagManager(
                    result,
                    allTagsToAddInRegistry.getOrDefault(resourceKey, Map.of()),
                    allTagsToRemoveInRegistry.getOrDefault(resourceKey, Map.of())
                );
                newResults.add(IInjectingTags.createLoadResult(resourceKey, r.getFirst()));
                int[] iResults = r.getSecond();
                i[0] += iResults[0];
                i[1] += iResults[1];
                i[2] += iResults[2];
            } else {
                newResults.add(result);
            }
        }
        this.results = newResults.stream().toList();
        return i;
    }
}

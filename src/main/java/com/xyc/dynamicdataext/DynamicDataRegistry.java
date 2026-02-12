package com.xyc.dynamicdataext;

import com.xyc.dynamicdataext.base.Module;
import com.xyc.dynamicdataext.base.RecipeEntry;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

/**
 * 所有扩展模组共享
 */
public final class DynamicDataRegistry {
    private static final Set<ModuleConfig> configs = new LinkedHashSet<>();

    public static void registerConfig(ModuleConfig config) {
        configs.add(config);
    }

    public static Pair<Set<RecipeEntry>, Set<ResourceLocation>> getAllRecipesToUpdate() {
        Set<RecipeEntry> recipesToAdd = new LinkedHashSet<>();
        Set<ResourceLocation> recipesToRemove = new LinkedHashSet<>();
        for (ModuleConfig config : configs) {
            for (Module module : config.getModules()) {
                if (config.isModuleEnabled(module.getId())) {
                    recipesToAdd.addAll(module.gatherRecipesToAdd());
                    recipesToRemove.addAll(module.gatherRecipesToRemove());
                }
            }
        }
        return Pair.of(recipesToAdd, recipesToRemove);
    }

    public static Pair<Map<TagKey<?>, Set<Holder<?>>>, Map<TagKey<?>, Set<Holder<?>>>> getAllTagsToUpdate() {
        Map<TagKey<?>, Set<Holder<?>>> tagsToAdd = new LinkedHashMap<>();
        Map<TagKey<?>, Set<Holder<?>>> tagsToRemove = new LinkedHashMap<>();
        for (ModuleConfig config : configs) {
            for (Module module : config.getModules()) {
                if (config.isModuleEnabled(module.getId())) {
                    tagsToAdd.putAll(module.gatherTagsToAdd());
                    tagsToRemove.putAll(module.gatherTagsToRemove());
                }
            }
        }
        return Pair.of(tagsToAdd, tagsToRemove);
    }
}

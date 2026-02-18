package com.xyc.dynamicdataext;

import com.xyc.dynamicdataext.base.Module;
import com.xyc.dynamicdataext.base.RecipeEntry;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import org.apache.commons.lang3.tuple.Pair;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * 所有扩展模组共享
 */
public final class DynamicDataRegistry {
    private static final Map<String, DynamicDataConfig> ddConfigs = new LinkedHashMap<>();

    public static void registerConfig(DynamicDataConfig config) {
        ddConfigs.put(config.getNamespace(), config);
    }

    public static Pair<Set<RecipeEntry>, Set<ResourceLocation>> getAllRecipesToUpdate() {
        Set<RecipeEntry> recipesToAdd = new LinkedHashSet<>();
        Set<ResourceLocation> recipesToRemove = new LinkedHashSet<>();
        for (DynamicDataConfig ddConfig : ddConfigs.values()) {
            for (Module module : ddConfig.getModules()) {
                if (ddConfig.isModuleEnabled(module.getModuleId())) {
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
        for (DynamicDataConfig ddConfig : ddConfigs.values()) {
            for (Module module : ddConfig.getModules()) {
                if (ddConfig.isModuleEnabled(module.getModuleId())) {
                    tagsToAdd.putAll(module.gatherTagsToAdd());
                    tagsToRemove.putAll(module.gatherTagsToRemove());
                }
            }
        }
        return Pair.of(tagsToAdd, tagsToRemove);
    }

    public static boolean autoReloading() {
        return (boolean) ddConfigs.get(DynamicDataMain.MOD_ID)
                                  .getModule("common")
                                  .getConfig()
                                  .getOption("auto_reload")
                                  .getValue();
    }
}

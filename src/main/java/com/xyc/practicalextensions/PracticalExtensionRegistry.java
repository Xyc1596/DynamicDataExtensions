package com.xyc.practicalextensions;

import com.xyc.practicalextensions.base.Module;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.fml.common.EventBusSubscriber;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 所有扩展模组共享
 */
@EventBusSubscriber(modid = ModMain.MOD_ID)
public final class PracticalExtensionRegistry {
    private static final Set<ModuleConfig> configs = new HashSet<>();

    public static void registerConfig(ModuleConfig config) {
        configs.add(config);
    }

    public static Pair<Set<RecipeHolder<Recipe<?>>>, Set<ResourceLocation>> getAllRecipesToUpdate() {
        Set<RecipeHolder<Recipe<?>>> recipesToAdd = new HashSet<>();
        Set<ResourceLocation> recipesToRemove = new HashSet<>();
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
        Map<TagKey<?>, Set<Holder<?>>> tagsToAdd = new HashMap<>();
        Map<TagKey<?>, Set<Holder<?>>> tagsToRemove = new HashMap<>();
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

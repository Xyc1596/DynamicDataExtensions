package com.xyc.practicalextensions;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashSet;
import java.util.Set;

/**
 * 所有扩展模组共享
 */
public final class PracticalExtensionRegistry {
    public static final Set<ModuleConfig> CONFIGS = new HashSet<>();

    public static void registerConfig(ModuleConfig config) {
        CONFIGS.add(config);
    }

    public static Pair<Set<RecipeHolder<Recipe<?>>>, Set<ResourceLocation>> getAllRecipesToUpdate() {
        Set<RecipeHolder<Recipe<?>>> recipesToAdd = new HashSet<>();
        Set<ResourceLocation> recipesToRemove = new HashSet<>();
        CONFIGS.forEach(
            config -> config.MODULES
                .stream()
                .filter(module -> config.isModuleEnabled(module.ID))
                .forEach(
                    module -> {
                        recipesToAdd.addAll(module.gatherRecipesToAdd());
                        recipesToRemove.addAll(module.gatherRecipesToRemove());
                    }
                )
        );
        return Pair.of(recipesToAdd, recipesToRemove);
    }
}

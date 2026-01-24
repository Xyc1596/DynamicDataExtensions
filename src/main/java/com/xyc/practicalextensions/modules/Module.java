package com.xyc.practicalextensions.modules;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.Map;
import java.util.Set;

public abstract class Module {
    public final String ID;
    public final String NAMESPACE;

    protected Module(String namespace, String id) {
        ID = id;
        NAMESPACE = namespace;
    }

    public Set<ResourceLocation> gatherRecipesToRemove() {
        return Set.of();
    }

    public Set<RecipeHolder<Recipe<?>>> gatherRecipesToAdd() {
        return Set.of();
    }

    public final String getOptionTranslationKey() {
        return "option." + NAMESPACE + "." + ID;
    }

    public Map<String, String> getOptionTranslations() {
        return Map.of();
    }

    public final String getTooltipTranslationKey() {
        return "tooltip." + NAMESPACE + "." + ID;
    }

    public Map<String, String> getTooltipTranslations() {
        return Map.of();
    }
}

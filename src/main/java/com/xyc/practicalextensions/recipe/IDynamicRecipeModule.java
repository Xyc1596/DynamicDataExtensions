package com.xyc.practicalextensions.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.Collection;

public interface IDynamicRecipeModule {
    boolean condition();
    Collection<RecipeHolder<Recipe<?>>> gatherRecipesToAdd();
    Collection<ResourceLocation> gatherRecipesToRemove();
}

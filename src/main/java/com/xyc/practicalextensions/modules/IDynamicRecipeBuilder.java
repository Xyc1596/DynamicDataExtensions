package com.xyc.practicalextensions.modules;

import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;

public interface IDynamicRecipeBuilder {
    /**
     * @see net.minecraft.data.recipes.RecipeBuilder#save(RecipeOutput, ResourceLocation)
     */
    Recipe<?> practicalextensions$toRecipe(ResourceLocation location);
}

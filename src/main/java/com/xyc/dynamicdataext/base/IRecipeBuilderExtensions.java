package com.xyc.dynamicdataext.base;

import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;

public interface IRecipeBuilderExtensions {
    /**
     * @see net.minecraft.data.recipes.RecipeBuilder#save(RecipeOutput, ResourceLocation)
     */
    Recipe<?> dynamicdataext$toRecipe(ResourceLocation location);
}

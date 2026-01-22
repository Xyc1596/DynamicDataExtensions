package com.xyc.practicalextensions.recipe.cooking;

import com.xyc.practicalextensions.config.ModConfig;
import com.xyc.practicalextensions.recipe.DynamicRecipeUtils;
import com.xyc.practicalextensions.recipe.IDynamicRecipeModule;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.Collection;
import java.util.List;

public class LeatherFromRottenFlesh implements IDynamicRecipeModule {
    @Override
    public boolean condition() {
        return ModConfig.leatherFromRottenFlesh;
    }

    @Override
    public Collection<RecipeHolder<Recipe<?>>> gatherRecipesToAdd() {
        return DynamicRecipeUtils.createSmokingAll(
            "leather_from_rotten_flesh",
            Ingredient.of(Items.ROTTEN_FLESH),
            RecipeCategory.MISC,
            Items.LEATHER,
            .1f,
            200
        );
    }

    @Override
    public Collection<ResourceLocation> gatherRecipesToRemove() {
        return List.of();
    }
}

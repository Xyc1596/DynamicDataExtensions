package com.xyc.practicalextensions.utils;

import com.xyc.practicalextensions.modules.IDynamicRecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.Set;

@SuppressWarnings("unused")
public final class Cooking {
    public static RecipeHolder<Recipe<?>> createSmelting(
        String id,
        Ingredient ingredient,
        RecipeCategory category,
        Item result,
        float experience,
        int cookingTime
    ) {
        ResourceLocation location = Utils.resourceLocation(id);
        return new RecipeHolder<>(
            location,
            ((IDynamicRecipeBuilder) SimpleCookingRecipeBuilder.smelting(
                ingredient, category, result, experience, cookingTime
            )).practicalextensions$toRecipe(location)
        );
    }

    public static RecipeHolder<Recipe<?>> createSmoking(
        String id,
        Ingredient ingredient,
        RecipeCategory category,
        Item result,
        float experience,
        int cookingTime
    ) {
        ResourceLocation location = Utils.resourceLocation(id);
        return new RecipeHolder<>(
            location,
            ((IDynamicRecipeBuilder) SimpleCookingRecipeBuilder.smoking(
                ingredient, category, result, experience, cookingTime
            )).practicalextensions$toRecipe(location)
        );
    }

    public static RecipeHolder<Recipe<?>> createCampfire(
        String id,
        Ingredient ingredient,
        RecipeCategory category,
        Item result,
        float experience,
        int cookingTime
    ) {
        ResourceLocation location = Utils.resourceLocation(id);
        return new RecipeHolder<>(
            location,
            ((IDynamicRecipeBuilder) SimpleCookingRecipeBuilder.campfireCooking(
                ingredient, category, result, experience, cookingTime
            )).practicalextensions$toRecipe(location)
        );
    }

    public static RecipeHolder<Recipe<?>> createBlasting(
        String id,
        Ingredient ingredient,
        RecipeCategory category,
        Item result,
        float experience,
        int cookingTime
    ) {
        ResourceLocation location = Utils.resourceLocation(id);
        return new RecipeHolder<>(
            location,
            ((IDynamicRecipeBuilder) SimpleCookingRecipeBuilder.blasting(
                ingredient, category, result, experience, cookingTime
            )).practicalextensions$toRecipe(location)
        );
    }

    public static Set<RecipeHolder<Recipe<?>>> createSmokingAll(
        String id,
        Ingredient ingredient,
        RecipeCategory category,
        Item result,
        float experience,
        int cookingTimeInFurnace
    ) {
        return Set.of(
            createSmelting(id + "_smelting", ingredient, category, result, experience, cookingTimeInFurnace),
            createSmoking(id + "_smoking", ingredient, category, result, experience, cookingTimeInFurnace / 2),
            createCampfire(id + "_campfire", ingredient, category, result, experience, cookingTimeInFurnace * 3)
        );
    }

    public static Set<RecipeHolder<Recipe<?>>> createBlastingAll(
        String id,
        Ingredient ingredient,
        RecipeCategory category,
        Item result,
        float experience,
        int cookingTimeInFurnace
    ) {
        return Set.of(
            createSmelting(id + "_smelting", ingredient, category, result, experience, cookingTimeInFurnace),
            createBlasting(id + "_blasting", ingredient, category, result, experience, cookingTimeInFurnace / 2)
        );
    }
}

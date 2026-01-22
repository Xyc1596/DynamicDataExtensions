package com.xyc.practicalextensions.recipe;

import com.xyc.practicalextensions.ModMain;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.Collection;
import java.util.Set;

@SuppressWarnings("unused")
public class DynamicRecipeUtils {
    public static RecipeHolder<Recipe<?>> createSmelting(
        String id,
        Ingredient ingredient,
        RecipeCategory category,
        Item result,
        float experience,
        int cookingTime
    ) {
        return new RecipeHolder<>(
            ModMain.resourceLocation(id),
            ((IDynamicRecipeBuilder) SimpleCookingRecipeBuilder.smelting(
                ingredient, category, result, experience, cookingTime
            )).practicalextensions$toRecipe()
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
        return new RecipeHolder<>(
            ModMain.resourceLocation(id),
            ((IDynamicRecipeBuilder) SimpleCookingRecipeBuilder.smoking(
                ingredient, category, result, experience, cookingTime
            )).practicalextensions$toRecipe()
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
        return new RecipeHolder<>(
            ModMain.resourceLocation(id),
            ((IDynamicRecipeBuilder) SimpleCookingRecipeBuilder.campfireCooking(
                ingredient, category, result, experience, cookingTime
            )).practicalextensions$toRecipe()
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
        return new RecipeHolder<>(
            ModMain.resourceLocation(id),
            ((IDynamicRecipeBuilder) SimpleCookingRecipeBuilder.blasting(
                ingredient, category, result, experience, cookingTime
            )).practicalextensions$toRecipe()
        );
    }

    public static Collection<RecipeHolder<Recipe<?>>> createSmokingAll(
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

    public static Collection<RecipeHolder<Recipe<?>>> createBlastingAll(
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

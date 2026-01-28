package com.xyc.practicalextensions.base;

import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Set;

@SuppressWarnings("unused")
public final class ModuleUtils {
    @ParametersAreNonnullByDefault
    public static RecipeHolder<Recipe<?>> createSmelting(
        String namespace,
        String id,
        Ingredient ingredient,
        RecipeCategory category,
        Item result,
        float experience,
        int cookingTime
    ) {
        return createSmelting(
            ResourceLocation.fromNamespaceAndPath(namespace, id),
            ingredient,
            category,
            result,
            experience,
            cookingTime
        );
    }

    @ParametersAreNonnullByDefault
    public static RecipeHolder<Recipe<?>> createSmelting(
        ResourceLocation location,
        Ingredient ingredient,
        RecipeCategory category,
        Item result,
        float experience,
        int cookingTime
    ) {
        return new RecipeHolder<>(
            location,
            ((IDynamicRecipeBuilder) SimpleCookingRecipeBuilder.smelting(
                ingredient, category, result, experience, cookingTime
            )).practicalextensions$toRecipe(location)
        );
    }

    @ParametersAreNonnullByDefault
    public static RecipeHolder<Recipe<?>> createSmoking(
        String namespace,
        String id,
        Ingredient ingredient,
        RecipeCategory category,
        Item result,
        float experience,
        int cookingTime
    ) {
        return createSmoking(
            ResourceLocation.fromNamespaceAndPath(namespace, id),
            ingredient,
            category,
            result,
            experience,
            cookingTime
        );
    }

    @ParametersAreNonnullByDefault
    public static RecipeHolder<Recipe<?>> createSmoking(
        ResourceLocation location,
        Ingredient ingredient,
        RecipeCategory category,
        Item result,
        float experience,
        int cookingTime
    ) {
        return new RecipeHolder<>(
            location,
            ((IDynamicRecipeBuilder) SimpleCookingRecipeBuilder.smoking(
                ingredient, category, result, experience, cookingTime
            )).practicalextensions$toRecipe(location)
        );
    }

    @ParametersAreNonnullByDefault
    public static RecipeHolder<Recipe<?>> createCampfire(
        String namespace,
        String id,
        Ingredient ingredient,
        RecipeCategory category,
        Item result,
        float experience,
        int cookingTime
    ) {
        return createCampfire(
            ResourceLocation.fromNamespaceAndPath(namespace, id),
            ingredient,
            category,
            result,
            experience,
            cookingTime
        );
    }

    @ParametersAreNonnullByDefault
    public static RecipeHolder<Recipe<?>> createCampfire(
        ResourceLocation location,
        Ingredient ingredient,
        RecipeCategory category,
        Item result,
        float experience,
        int cookingTime
    ) {
        return new RecipeHolder<>(
            location,
            ((IDynamicRecipeBuilder) SimpleCookingRecipeBuilder.campfireCooking(
                ingredient, category, result, experience, cookingTime
            )).practicalextensions$toRecipe(location)
        );
    }

    @ParametersAreNonnullByDefault
    public static RecipeHolder<Recipe<?>> createBlasting(
        String namespace,
        String id,
        Ingredient ingredient,
        RecipeCategory category,
        Item result,
        float experience,
        int cookingTime
    ) {
        return createBlasting(
            ResourceLocation.fromNamespaceAndPath(namespace, id),
            ingredient,
            category,
            result,
            experience,
            cookingTime
        );
    }

    @ParametersAreNonnullByDefault
    public static RecipeHolder<Recipe<?>> createBlasting(
        ResourceLocation location,
        Ingredient ingredient,
        RecipeCategory category,
        Item result,
        float experience,
        int cookingTime
    ) {
        return new RecipeHolder<>(
            location,
            ((IDynamicRecipeBuilder) SimpleCookingRecipeBuilder.blasting(
                ingredient, category, result, experience, cookingTime
            )).practicalextensions$toRecipe(location)
        );
    }

    @ParametersAreNonnullByDefault
    public static Set<RecipeHolder<Recipe<?>>> createSmokingAll(
        String namespace,
        String id,
        Ingredient ingredient,
        RecipeCategory category,
        Item result,
        float experience,
        int cookingTimeInFurnace
    ) {
        return Set.of(
            createSmelting(
                namespace, id + "_smelting", ingredient, category, result, experience, cookingTimeInFurnace
            ),
            createSmoking(
                namespace, id + "_smoking", ingredient, category, result, experience, cookingTimeInFurnace / 2
            ),
            createCampfire(
                namespace, id + "_campfire", ingredient, category, result, experience, cookingTimeInFurnace * 3
            )
        );
    }

    @ParametersAreNonnullByDefault
    public static Set<RecipeHolder<Recipe<?>>> createBlastingAll(
        String namespace,
        String id,
        Ingredient ingredient,
        RecipeCategory category,
        Item result,
        float experience,
        int cookingTimeInFurnace
    ) {
        return Set.of(
            createSmelting(
                namespace, id + "_smelting", ingredient, category, result, experience, cookingTimeInFurnace
            ),
            createBlasting(
                namespace, id + "_blasting", ingredient, category, result, experience, cookingTimeInFurnace / 2
            )
        );
    }

    @ParametersAreNonnullByDefault
    public static RecipeHolder<Recipe<?>> createRecipeHolder(String namespace, String recipeId, RecipeBuilder builder) {
        return createRecipeHolder(ResourceLocation.fromNamespaceAndPath(namespace, recipeId), builder);
    }

    @ParametersAreNonnullByDefault
    public static RecipeHolder<Recipe<?>> createRecipeHolder(ResourceLocation recipeLocation, RecipeBuilder builder) {
        return new RecipeHolder<>(
            recipeLocation,
            ((IDynamicRecipeBuilder) builder).practicalextensions$toRecipe(recipeLocation)
        );
    }

    @ParametersAreNonnullByDefault
    public static RecipeHolder<Recipe<?>> createRecipeHolder(
        String namespace,
        String recipeId,
        SpecialRecipeBuilder builder
    ) {
        return createRecipeHolder(ResourceLocation.fromNamespaceAndPath(namespace, recipeId), builder);
    }

    @ParametersAreNonnullByDefault
    public static RecipeHolder<Recipe<?>> createRecipeHolder(
        ResourceLocation recipeLocation,
        SpecialRecipeBuilder builder
    ) {
        return new RecipeHolder<>(
            recipeLocation,
            ((IDynamicRecipeBuilder) builder).practicalextensions$toRecipe(recipeLocation)
        );
    }
}

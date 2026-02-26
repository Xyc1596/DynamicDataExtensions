package com.xyc.dynamicdataext.utils;

import com.xyc.dynamicdataext.base.DynamicRecipeEntry;
import com.xyc.dynamicdataext.base.IRecipeBuilderExtensions;
import com.xyc.dynamicdataext.base.Module;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.LinkedHashSet;
import java.util.Set;

@SuppressWarnings("unused")
public final class RecipeUtils {
    @ParametersAreNonnullByDefault
    public static DynamicRecipeEntry createSmelting(
        Module module,
        String recipeId,
        Ingredient ingredient,
        RecipeCategory category,
        Item result,
        float experience, int cookingTime
    ) {
        ResourceLocation location = LocationUtils.createContentLocationWithModuleId(module, recipeId);
        return new DynamicRecipeEntry(
            location,
            ((IRecipeBuilderExtensions) SimpleCookingRecipeBuilder
                .smelting(ingredient, category, result, experience, cookingTime)
                .unlockedBy("has_ingredient", CriterionUtils.hasSingleIngredient(ingredient))
            ).dynamicdataext$toRecipe(location)
        );
    }

    @ParametersAreNonnullByDefault
    public static DynamicRecipeEntry createSmoking(
        Module module,
        String recipeId,
        Ingredient ingredient,
        RecipeCategory category,
        Item result,
        float experience, int cookingTime
    ) {
        ResourceLocation location = LocationUtils.createContentLocationWithModuleId(module, recipeId);
        return new DynamicRecipeEntry(
            location,
            ((IRecipeBuilderExtensions) SimpleCookingRecipeBuilder
                .smoking(ingredient, category, result, experience, cookingTime)
                .unlockedBy("has_ingredient", CriterionUtils.hasSingleIngredient(ingredient))
            ).dynamicdataext$toRecipe(location)
        );
    }

    @ParametersAreNonnullByDefault
    public static DynamicRecipeEntry createCampfire(
        Module module,
        String recipeId,
        Ingredient ingredient,
        RecipeCategory category,
        Item result,
        float experience,
        int cookingTime
    ) {
        ResourceLocation location = LocationUtils.createContentLocationWithModuleId(module, recipeId);
        return new DynamicRecipeEntry(
            location,
            ((IRecipeBuilderExtensions) SimpleCookingRecipeBuilder
                .campfireCooking(ingredient, category, result, experience, cookingTime)
                .unlockedBy("has_ingredient", CriterionUtils.hasSingleIngredient(ingredient))
            ).dynamicdataext$toRecipe(location)
        );
    }

    @ParametersAreNonnullByDefault
    public static DynamicRecipeEntry createBlasting(
        Module module,
        String recipeId,
        Ingredient ingredient,
        RecipeCategory category,
        Item result,
        float experience,
        int cookingTime
    ) {
        ResourceLocation location = LocationUtils.createContentLocationWithModuleId(module, recipeId);
        return new DynamicRecipeEntry(
            location,
            ((IRecipeBuilderExtensions) SimpleCookingRecipeBuilder
                .blasting(ingredient, category, result, experience, cookingTime)
                .unlockedBy("has_ingredient", CriterionUtils.hasSingleIngredient(ingredient))
            ).dynamicdataext$toRecipe(location)
        );
    }

    @ParametersAreNonnullByDefault
    public static Set<DynamicRecipeEntry> createSmokingAll(
        Module module,
        String recipeId,
        Ingredient ingredient,
        RecipeCategory category,
        Item result,
        float experience,
        int cookingTimeInFurnace
    ) {
        Set<DynamicRecipeEntry> output = new LinkedHashSet<>();
        output.add(createSmelting(
            module, recipeId + "_smelting", ingredient, category, result, experience, cookingTimeInFurnace
        ));
        output.add(createSmoking(
            module, recipeId + "_smoking", ingredient, category, result, experience, cookingTimeInFurnace / 2
        ));
        output.add(createCampfire(
            module, recipeId + "_campfire", ingredient, category, result, experience, cookingTimeInFurnace * 3
        ));
        return output;
    }

    @ParametersAreNonnullByDefault
    public static Set<DynamicRecipeEntry> createBlastingAll(
        Module module,
        String recipeId,
        Ingredient ingredient,
        RecipeCategory category,
        Item result,
        float experience,
        int cookingTimeInFurnace
    ) {
        Set<DynamicRecipeEntry> output = new LinkedHashSet<>();
        output.add(createSmelting(
            module, recipeId + "_smelting", ingredient, category, result, experience, cookingTimeInFurnace
        ));
        output.add(createBlasting(
            module, recipeId + "_blasting", ingredient, category, result, experience, cookingTimeInFurnace / 2
        ));
        return output;
    }

    @ParametersAreNonnullByDefault
    public static DynamicRecipeEntry createRecipeEntry(Module module, String recipeId, RecipeBuilder builder) {
        ResourceLocation location = LocationUtils.createContentLocationWithModuleId(module, recipeId);
        return new DynamicRecipeEntry(location, ((IRecipeBuilderExtensions) builder).dynamicdataext$toRecipe(location));
    }
}

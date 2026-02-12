package com.xyc.dynamicdataext.utils;

import com.xyc.dynamicdataext.base.IDynamicRecipeBuilder;
import com.xyc.dynamicdataext.base.Module;
import com.xyc.dynamicdataext.base.RecipeEntry;
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
    public static RecipeEntry createSmelting(
        Module module,
        String recipeId,
        Ingredient ingredient,
        RecipeCategory category,
        Item result,
        float experience, int cookingTime
    ) {
        ResourceLocation location = ResourceLocationUtils.getContentLocationWithModuleId(module, recipeId);
        return new RecipeEntry(
            location,
            ((IDynamicRecipeBuilder) SimpleCookingRecipeBuilder
                .smelting(ingredient, category, result, experience, cookingTime)
                .unlockedBy("has_ingredient", CriterionUtils.hasSingleIngredient(ingredient))
            ).dynamicdataext$toRecipe(location)
        );
    }

    @ParametersAreNonnullByDefault
    public static RecipeEntry createSmoking(
        Module module,
        String recipeId,
        Ingredient ingredient,
        RecipeCategory category,
        Item result,
        float experience, int cookingTime
    ) {
        ResourceLocation location = ResourceLocationUtils.getContentLocationWithModuleId(module, recipeId);
        return new RecipeEntry(
            location,
            ((IDynamicRecipeBuilder) SimpleCookingRecipeBuilder
                .smoking(ingredient, category, result, experience, cookingTime)
                .unlockedBy("has_ingredient", CriterionUtils.hasSingleIngredient(ingredient))
            ).dynamicdataext$toRecipe(location)
        );
    }

    @ParametersAreNonnullByDefault
    public static RecipeEntry createCampfire(
        Module module,
        String recipeId,
        Ingredient ingredient,
        RecipeCategory category,
        Item result,
        float experience,
        int cookingTime
    ) {
        ResourceLocation location = ResourceLocationUtils.getContentLocationWithModuleId(module, recipeId);
        return new RecipeEntry(
            location,
            ((IDynamicRecipeBuilder) SimpleCookingRecipeBuilder
                .campfireCooking(ingredient, category, result, experience, cookingTime)
                .unlockedBy("has_ingredient", CriterionUtils.hasSingleIngredient(ingredient))
            ).dynamicdataext$toRecipe(location)
        );
    }

    @ParametersAreNonnullByDefault
    public static RecipeEntry createBlasting(
        Module module,
        String recipeId,
        Ingredient ingredient,
        RecipeCategory category,
        Item result,
        float experience,
        int cookingTime
    ) {
        ResourceLocation location = ResourceLocationUtils.getContentLocationWithModuleId(module, recipeId);
        return new RecipeEntry(
            location,
            ((IDynamicRecipeBuilder) SimpleCookingRecipeBuilder
                .blasting(ingredient, category, result, experience, cookingTime)
                .unlockedBy("has_ingredient", CriterionUtils.hasSingleIngredient(ingredient))
            ).dynamicdataext$toRecipe(location)
        );
    }

    @ParametersAreNonnullByDefault
    public static Set<RecipeEntry> createSmokingAll(
        Module module,
        String recipeId,
        Ingredient ingredient,
        RecipeCategory category,
        Item result,
        float experience,
        int cookingTimeInFurnace
    ) {
        Set<RecipeEntry> output = new LinkedHashSet<>();
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
    public static Set<RecipeEntry> createBlastingAll(
        Module module,
        String recipeId,
        Ingredient ingredient,
        RecipeCategory category,
        Item result,
        float experience,
        int cookingTimeInFurnace
    ) {
        Set<RecipeEntry> output = new LinkedHashSet<>();
        output.add(createSmelting(
                module, recipeId + "_smelting", ingredient, category, result, experience, cookingTimeInFurnace
        ));
        output.add(createBlasting(
            module, recipeId + "_blasting", ingredient, category, result, experience, cookingTimeInFurnace / 2
        ));
        return output;
    }

    @ParametersAreNonnullByDefault
    public static RecipeEntry createRecipeEntry(Module module, String recipeId, RecipeBuilder builder) {
        ResourceLocation location = ResourceLocationUtils.getContentLocationWithModuleId(module, recipeId);
        return new RecipeEntry(location, ((IDynamicRecipeBuilder) builder).dynamicdataext$toRecipe(location));
    }
}

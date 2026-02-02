package com.xyc.dynamicdataext.mixins.recipes;

import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;
import java.util.Objects;

@Mixin(ShapelessRecipeBuilder.class)
public abstract class ShapelessRecipeBuilderMixin implements RecipeBuilderMixin {
    @Shadow
    @Nullable
    private String group;
    @Shadow
    @Final
    private RecipeCategory category;
    @Shadow
    @Final
    private ItemStack resultStack;
    @Shadow
    @Final
    private NonNullList<Ingredient> ingredients;

    /**
     * @see ShapelessRecipeBuilder#save(RecipeOutput, ResourceLocation)
     */
    @Override
    public Recipe<?> dynamicdataext$toRecipe(ResourceLocation location) {
        return new ShapelessRecipe(
            Objects.requireNonNullElse(this.group, ""),
            RecipeBuilder.determineBookCategory(this.category),
            this.resultStack,
            this.ingredients
        );
    }
}

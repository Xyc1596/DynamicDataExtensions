package com.xyc.practicalextensions.mixins;

import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Objects;

@Mixin(SimpleCookingRecipeBuilder.class)
public abstract class SimpleCookingRecipeBuilderMixin implements IRecipeBuilderMixin {
    @Shadow
    @Final
    private AbstractCookingRecipe.Factory<?> factory;
    @Shadow
    private String group;
    @Shadow
    @Final
    private CookingBookCategory bookCategory;
    @Shadow
    @Final
    private Ingredient ingredient;
    @Shadow
    @Final
    private ItemStack stackResult;
    @Shadow
    @Final
    private float experience;
    @Shadow
    @Final
    private int cookingTime;

    /**
     * @see SimpleCookingRecipeBuilder#save(RecipeOutput, ResourceLocation)
     */
    @Override
    public Recipe<?> practicalextensions$toRecipe(ResourceLocation location) {
        return factory.create(
            Objects.requireNonNullElse(this.group, ""),
            this.bookCategory,
            this.ingredient,
            this.stackResult,
            this.experience,
            this.cookingTime
        );
    }
}

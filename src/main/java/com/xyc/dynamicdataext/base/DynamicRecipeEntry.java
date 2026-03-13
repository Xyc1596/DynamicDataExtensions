package com.xyc.dynamicdataext.base;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;

public class DynamicRecipeEntry {
    protected final ResourceLocation id;
    protected final Recipe<?> recipe;

    public DynamicRecipeEntry(ResourceLocation id, Recipe<?> recipe) {
        this.id = id;
        this.recipe = recipe;
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public RecipeHolder<?> getValue() {
        return new RecipeHolder<>(this.id, this.recipe);
    }

    public RecipeType<?> getType() {
        return this.recipe.getType();
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }
}

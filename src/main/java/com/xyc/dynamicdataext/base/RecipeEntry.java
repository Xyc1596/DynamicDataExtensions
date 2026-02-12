package com.xyc.dynamicdataext.base;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.Map;

public class RecipeEntry {
    protected final ResourceLocation id;
    protected final Recipe<?> recipe;
    protected final RecipeHolder<Recipe<?>> holder;

    public RecipeEntry(ResourceLocation id, Recipe<?> recipe) {
        this.id = id;
        this.recipe = recipe;
        this.holder = new RecipeHolder<>(id, recipe);
    }

    public Map.Entry<RecipeType<?>, RecipeHolder<?>> toMapEntryByType() {
        return Map.entry(this.recipe.getType(), this.holder);
    }

    public Map.Entry<ResourceLocation, RecipeHolder<?>> toMapEntryByName() {
        return Map.entry(this.id, this.holder);
    }
}

package com.xyc.dynamicdataext.base;

import com.google.common.collect.Multimap;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.Map;

public interface IRecipeManagerExtensions {
    HolderLookup.Provider getRegistries();

    Map<ResourceLocation, RecipeHolder<?>> getByName();

    void setByType(Multimap<RecipeType<?>, RecipeHolder<?>> byType);

    void setByName(Map<ResourceLocation, RecipeHolder<?>> byName);

    default ItemStack getResultItemStack(Recipe<?> recipe) {
        return recipe.getResultItem(this.getRegistries());
    }
}

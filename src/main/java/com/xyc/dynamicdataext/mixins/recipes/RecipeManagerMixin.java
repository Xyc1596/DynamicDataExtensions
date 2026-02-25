package com.xyc.dynamicdataext.mixins.recipes;

import com.google.common.collect.Multimap;
import com.xyc.dynamicdataext.base.IRecipeManagerExtensions;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(RecipeManager.class)
public interface RecipeManagerMixin extends IRecipeManagerExtensions {
    @Override
    @Accessor("registries")
    HolderLookup.Provider getRegistries();

    @Override
    @Accessor("byName")
    Map<ResourceLocation, RecipeHolder<?>> getByName();

    @Override
    @Accessor("byType")
    void setByType(Multimap<RecipeType<?>, RecipeHolder<?>> byType);

    @Override
    @Accessor("byName")
    void setByName(Map<ResourceLocation, RecipeHolder<?>> byName);
}

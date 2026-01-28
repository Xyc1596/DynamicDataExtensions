package com.xyc.practicalextensions.mixins;

import com.xyc.practicalextensions.base.IDynamicRecipeBuilder;
import net.minecraft.data.recipes.RecipeBuilder;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(RecipeBuilder.class)
public interface IRecipeBuilderMixin extends IDynamicRecipeBuilder {
}

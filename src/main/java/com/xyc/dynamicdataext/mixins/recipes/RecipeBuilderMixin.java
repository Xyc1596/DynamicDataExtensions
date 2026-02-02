package com.xyc.dynamicdataext.mixins.recipes;

import com.xyc.dynamicdataext.base.IDynamicRecipeBuilder;
import net.minecraft.data.recipes.RecipeBuilder;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(RecipeBuilder.class)
public interface RecipeBuilderMixin extends IDynamicRecipeBuilder {
}

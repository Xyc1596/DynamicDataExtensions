package com.xyc.dynamicdataext.mixins.recipes;

import com.xyc.dynamicdataext.base.IRecipeBuilderExtensions;
import net.minecraft.data.recipes.SmithingTrimRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.SmithingTrimRecipe;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SmithingTrimRecipeBuilder.class)
public abstract class SmithingTrimRecipeBuilderMixin implements IRecipeBuilderExtensions {
    @Shadow
    @Final
    private Ingredient template;
    @Shadow
    @Final
    private Ingredient base;
    @Shadow
    @Final
    private Ingredient addition;

    @Override
    public Recipe<?> dynamicdataext$toRecipe(ResourceLocation location) {
        return new SmithingTrimRecipe(this.template, this.base, this.addition);
    }
}

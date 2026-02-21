package com.xyc.dynamicdataext.mixins.recipes;

import com.xyc.dynamicdataext.base.IDynamicRecipeBuilder;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.SmithingTransformRecipe;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SmithingTransformRecipeBuilder.class)
public abstract class SmithingTransformRecipeBuilderMixin implements IDynamicRecipeBuilder {
    @Shadow
    @Final
    private Ingredient template;
    @Shadow
    @Final
    private Ingredient base;
    @Shadow
    @Final
    private Ingredient addition;
    @Shadow
    @Final
    private Item result;

    @Override
    public Recipe<?> dynamicdataext$toRecipe(ResourceLocation location) {
        return new SmithingTransformRecipe(this.template, this.base, this.addition, new ItemStack(this.result));
    }
}

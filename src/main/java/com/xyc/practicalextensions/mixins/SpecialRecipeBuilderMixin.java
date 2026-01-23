package com.xyc.practicalextensions.mixins;

import com.xyc.practicalextensions.modules.IDynamicRecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Recipe;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.Function;

@Mixin(SpecialRecipeBuilder.class)
public abstract class SpecialRecipeBuilderMixin implements IDynamicRecipeBuilder {
    @Shadow
    @Final
    private Function<CraftingBookCategory, Recipe<?>> factory;

    /**
     * @see SpecialRecipeBuilder#save(RecipeOutput, ResourceLocation)
     */
    @Override
    public Recipe<?> practicalextensions$toRecipe(ResourceLocation location) {
        return this.factory.apply(CraftingBookCategory.MISC);
    }
}

package com.xyc.practicalextensions.mixins;

import com.xyc.practicalextensions.utils.IDynamicRecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.SingleItemRecipe;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;
import java.util.Objects;

@Mixin(SingleItemRecipeBuilder.class)
public abstract class SingleItemRecipeBuilderMixin implements IDynamicRecipeBuilder {
    @Shadow
    @Final
    private SingleItemRecipe.Factory<?> factory;
    @Shadow
    @Nullable
    private String group;
    @Shadow
    @Final
    private Ingredient ingredient;
    @Shadow
    @Final
    private Item result;
    @Shadow
    @Final
    private int count;

    /**
     * @see SingleItemRecipeBuilder#save(RecipeOutput, ResourceLocation)
     */
    @Override
    public Recipe<?> practicalextensions$toRecipe(ResourceLocation location) {
        return this.factory.create(
            Objects.requireNonNullElse(this.group, ""),
            this.ingredient,
            new ItemStack(this.result, this.count)
        );
    }
}

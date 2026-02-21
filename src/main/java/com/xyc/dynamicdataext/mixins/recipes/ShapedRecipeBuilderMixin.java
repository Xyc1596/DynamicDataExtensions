package com.xyc.dynamicdataext.mixins.recipes;

import com.xyc.dynamicdataext.base.IDynamicRecipeBuilder;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;
import java.util.Objects;

@Mixin(ShapedRecipeBuilder.class)
public abstract class ShapedRecipeBuilderMixin implements IDynamicRecipeBuilder {
    @Shadow
    @Nullable
    private String group;
    @Shadow
    @Final
    private RecipeCategory category;
    @Shadow
    @Final
    private ItemStack resultStack;
    @Shadow
    private boolean showNotification;

    @Shadow
    @SuppressWarnings("SpellCheckingInspection")    // 666还有拼写错误
    protected abstract ShapedRecipePattern ensureValid(ResourceLocation loaction);

    /**
     * @see ShapedRecipeBuilder#save(RecipeOutput, ResourceLocation)
     */
    @Override
    public Recipe<?> dynamicdataext$toRecipe(ResourceLocation location) {
        return new ShapedRecipe(
            Objects.requireNonNullElse(this.group, ""),
            RecipeBuilder.determineBookCategory(this.category),
            this.ensureValid(location),
            this.resultStack,
            this.showNotification
        );
    }
}

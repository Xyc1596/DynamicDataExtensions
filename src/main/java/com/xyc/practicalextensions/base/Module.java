package com.xyc.practicalextensions.base;

import com.xyc.practicalextensions.lang.ModuleLang;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;

import javax.annotation.Nonnull;
import java.util.Set;

public abstract class Module {
    public final String ID;
    public final String NAMESPACE;
    protected final ModuleLang.TranslatableLang OPTION = buildOptionLang();
    protected final ModuleLang.TranslatableLang TOOLTIP = buildTooltipLang();

    protected Module(String namespace, String id) {
        ID = id;
        NAMESPACE = namespace;
    }

    @Nonnull
    public Set<ResourceLocation> gatherRecipesToRemove() {
        return Set.of();
    }

    @Nonnull
    public Set<RecipeHolder<Recipe<?>>> gatherRecipesToAdd() {
        return Set.of();
    }

    public final String getOptionTranslationKey() {
        return OPTION.getKey();
    }

    public final String getTooltipTranslationKey() {
        return TOOLTIP.getKey();
    }

    @Nonnull
    protected ModuleLang.TranslatableLang buildOptionLang() {
        return ModuleLang.TranslatableLang.of("option", NAMESPACE, ID);
    }

    @Nonnull
    protected ModuleLang.TranslatableLang buildTooltipLang() {
        return ModuleLang.TranslatableLang.of("tooltip", NAMESPACE, ID);
    }

    // public final RecipeHolder<Recipe<?>> createRecipeHolder(String recipeId, RecipeBuilder recipeBuilder) {
    //     ResourceLocation location = ResourceLocation.fromNamespaceAndPath(NAMESPACE, recipeId);
    //     return new RecipeHolder<>(
    //         location,
    //         (IDynamicRecipeBuilder) recipeBuilder
    //     )
    // }
}

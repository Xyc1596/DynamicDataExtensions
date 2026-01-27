package com.xyc.practicalextensions.modules.contents;

import com.xyc.practicalextensions.ModMain;
import com.xyc.practicalextensions.base.IDynamicRecipeBuilder;
import com.xyc.practicalextensions.base.Module;
import com.xyc.practicalextensions.lang.ModuleLang;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class WoolToString extends Module {
    public WoolToString() {
        super(ModMain.MOD_ID, "wool_and_carpet_to_string");
    }

    @Override
    public @NotNull Set<RecipeHolder<Recipe<?>>> gatherRecipesToAdd() {
        ResourceLocation location_wool = ResourceLocation.fromNamespaceAndPath(ModMain.MOD_ID, "wool_to_string");
        ResourceLocation location_carpet = ResourceLocation.fromNamespaceAndPath(ModMain.MOD_ID, "carpet_to_string");
        return Set.of(
            new RecipeHolder<>(
                location_wool,
                ((IDynamicRecipeBuilder) ShapelessRecipeBuilder
                    .shapeless(RecipeCategory.MISC, Items.STRING, 16)
                    .requires(
                        Ingredient.of(TagKey.create(Registries.ITEM, ResourceLocation.withDefaultNamespace("wool"))),
                        4
                    )
                    .requires(Items.FLINT))
                    .practicalextensions$toRecipe(location_wool)
            ),
            new RecipeHolder<>(
                location_carpet,
                ((IDynamicRecipeBuilder) ShapelessRecipeBuilder
                    .shapeless(RecipeCategory.MISC, Items.STRING, 16)
                    .requires(
                        Ingredient.of(TagKey.create(Registries.ITEM, ResourceLocation.withDefaultNamespace("carpet"))),
                        6
                    )
                    .requires(Items.FLINT))
                    .practicalextensions$toRecipe(location_carpet)
            )
        );
    }

    @Override
    protected @NotNull ModuleLang.TranslatableLang buildOptionLang() {
        return super.buildOptionLang();
    }

    @Override
    protected @NotNull ModuleLang.TranslatableLang buildTooltipLang() {
        return super.buildTooltipLang();
    }
}

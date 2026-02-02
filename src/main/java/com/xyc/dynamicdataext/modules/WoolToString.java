package com.xyc.dynamicdataext.modules;

import com.xyc.dynamicdataext.ModMain;
import com.xyc.dynamicdataext.base.Module;
import com.xyc.dynamicdataext.base.ModuleUtils;
import com.xyc.dynamicdataext.lang.ModuleLangBuilder;
import com.xyc.dynamicdataext.lang.TranslatableLang;
import net.minecraft.ChatFormatting;
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
        return Set.of(
            ModuleUtils.createRecipeHolder(
                this.namespace,
                "wool_to_string",
                ShapelessRecipeBuilder
                    .shapeless(RecipeCategory.MISC, Items.STRING, 4)
                    .requires(
                        Ingredient.of(TagKey.create(Registries.ITEM, ResourceLocation.withDefaultNamespace("wool"))),
                        4
                    )
                    .requires(Items.FLINT)
            ),
            ModuleUtils.createRecipeHolder(
                this.namespace,
                "carpet_to_string",
                ShapelessRecipeBuilder
                    .shapeless(RecipeCategory.MISC, Items.STRING, 4)
                    .requires(
                        Ingredient.of(TagKey.create(
                            Registries.ITEM,
                            ResourceLocation.withDefaultNamespace("wool_carpets"))
                        ),
                        6
                    )
                    .requires(Items.FLINT)
            )
        );
    }

    @Override
    protected @NotNull TranslatableLang buildOptionLang() {
        return ModuleLangBuilder.translatable("option", this.namespace, this.id)
                                .translation("zh_cn", "羊毛 & 地毯制线")
                                .translation("en_us", "Wool & Carpet to String")
                                .build();
    }

    @Override
    protected @NotNull TranslatableLang buildTooltipLang() {
        return ModuleLangBuilder.translatable("tooltip", this.namespace, this.id)
                                .translation("zh_cn", "4 羊毛 / 6 地毯 + 1 燧石合成 4 根线\n%s")
                                .translation("en_us", "Craft 4 strings with 4 wools / 6 carpets and 1 flint\n%s")
                                .child(
                                    ModuleLangBuilder.translatable("tooltip", this.namespace, this.id + "_1")
                                                     .translation("zh_cn", "灵感来源：不记得了 :(")
                                                     .translation("en_us", "Inspired by: I don't remember :(")
                                                     .format(ChatFormatting.ITALIC)
                                                     .build()
                                )
                                .build();
    }
}

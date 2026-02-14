package com.xyc.dynamicdataext.modules;

import com.xyc.dynamicdataext.DynamicDataMain;
import com.xyc.dynamicdataext.base.Module;
import com.xyc.dynamicdataext.base.RecipeEntry;
import com.xyc.dynamicdataext.lang.ModuleLangBuilder;
import com.xyc.dynamicdataext.lang.TranslatableLang;
import com.xyc.dynamicdataext.utils.CriterionUtils;
import com.xyc.dynamicdataext.utils.RecipeUtils;
import com.xyc.dynamicdataext.utils.ResourceLocationUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class WoolToString extends Module {
    public WoolToString() {
        super(DynamicDataMain.MOD_ID, "wool_and_carpet_to_string");
    }

    @Override
    public @NotNull Set<RecipeEntry> gatherRecipesToAdd() {
        final TagKey<Item> WOOLS = ResourceLocationUtils.createItemTagKey(
            ResourceLocationUtils.withDefaultNamespace("wool")
        );
        final TagKey<Item> CARPETS = ResourceLocationUtils.createItemTagKey(
            ResourceLocationUtils.withDefaultNamespace("wool_carpets")
        );

        return Set.of(
            RecipeUtils.createRecipeEntry(
                this,
                "wool_to_string",
                ShapelessRecipeBuilder
                    .shapeless(RecipeCategory.MISC, Items.STRING, 4)
                    .requires(Ingredient.of(WOOLS), 4)
                    .requires(Items.FLINT)
                    .unlockedBy("has_wool", CriterionUtils.hasTag(WOOLS))
                    .unlockedBy("has_flint", CriterionUtils.hasItems(Items.FLINT))
            ),
            RecipeUtils.createRecipeEntry(
                this,
                "carpet_to_string",
                ShapelessRecipeBuilder
                    .shapeless(RecipeCategory.MISC, Items.STRING, 4)
                    .requires(Ingredient.of(CARPETS), 6)
                    .requires(Items.FLINT)
                    .unlockedBy("has_carpet", CriterionUtils.hasTag(CARPETS))
                    .unlockedBy("has_flint", CriterionUtils.hasItems(Items.FLINT))
            )
        );
    }

    @Override
    protected @NotNull TranslatableLang buildOptionLang() {
        return this.getOptionLangBuilder()
                   .translation("zh_cn", "羊毛 & 地毯制线")
                   .translation("en_us", "Wool & Carpet to String")
                   .build();
    }

    @Override
    protected @NotNull TranslatableLang buildTooltipLang() {
        return this
            .getTooltipLangBuilder()
            .translation("zh_cn", "4 羊毛 / 6 地毯 + 1 燧石 -> 4 根线\n%s")
            .translation("en_us", "4 wools / 6 carpets + 1 flint -> 4 strings\n%s")
            .child(
                ModuleLangBuilder
                    .translatable("tooltip", this.namespace, this.id + "_1")
                    .translation("zh_cn", "灵感来源：不记得了 :(")
                    .translation("en_us", "Inspired by: I don't remember :(")
                    .format(ChatFormatting.ITALIC)
                    .build()
            )
            .build();
    }
}

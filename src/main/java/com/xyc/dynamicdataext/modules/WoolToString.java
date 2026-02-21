package com.xyc.dynamicdataext.modules;

import com.xyc.dynamicdataext.DynamicDataMain;
import com.xyc.dynamicdataext.base.Module;
import com.xyc.dynamicdataext.base.RecipeEntry;
import com.xyc.dynamicdataext.config.ModuleConfig;
import com.xyc.dynamicdataext.config.ModuleConfigBuilder;
import com.xyc.dynamicdataext.config.ModuleOptionBuilder;
import com.xyc.dynamicdataext.utils.ConfigUtils;
import com.xyc.dynamicdataext.utils.CriterionUtils;
import com.xyc.dynamicdataext.utils.RecipeUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.tags.ItemTags;
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
        return Set.of(
            RecipeUtils.createRecipeEntry(
                this,
                "wool_to_string",
                ShapelessRecipeBuilder
                    .shapeless(RecipeCategory.MISC, Items.STRING, 4)
                    .requires(Ingredient.of(ItemTags.WOOL), 4)
                    .requires(Items.FLINT)
                    .unlockedBy("has_wool", CriterionUtils.hasTag(ItemTags.WOOL))
                    .unlockedBy("has_flint", CriterionUtils.hasItems(Items.FLINT))
            ),
            RecipeUtils.createRecipeEntry(
                this,
                "carpet_to_string",
                ShapelessRecipeBuilder
                    .shapeless(RecipeCategory.MISC, Items.STRING, 4)
                    .requires(Ingredient.of(ItemTags.WOOL_CARPETS), 6)
                    .requires(Items.FLINT)
                    .unlockedBy("has_carpet", CriterionUtils.hasTag(ItemTags.WOOL_CARPETS))
                    .unlockedBy("has_flint", CriterionUtils.hasItems(Items.FLINT))
            )
        );
    }

    @Override
    protected @NotNull ModuleConfig buildConfig() {
        ModuleConfigBuilder builder = this.createConfigBuilder();
        ModuleOptionBuilder<Boolean> enabled = ConfigUtils.createEnabledOptionBuilderWithTitle(builder);
        return builder.setTitle(builder
            .getTitleLangBuilder()
            .translation("zh_cn", "羊毛 & 地毯制线")
            .translation("en_us", "Wool & Carpet to String")
            .build()
        ).defineEnabled(enabled
            .setDefaultValue(true)
            .setTooltip(enabled
                .getTooltipLangBuilder()
                .translation("zh_cn", "4 羊毛 / 6 地毯 + 1 燧石 → 4 根线\n%s")
                .translation("en_us", "4 wools / 6 carpets + 1 flint → 4 strings\n%s")
                .child(enabled
                    .createTooltipChildLangBuilder()
                    .translation("zh_cn", "灵感来源：不记得了 :(")
                    .translation("en_us", "Inspired by: I don't remember :(")
                    .format(ChatFormatting.ITALIC)
                    .build()
                ).build()
            ).build()
        ).build();
    }
}

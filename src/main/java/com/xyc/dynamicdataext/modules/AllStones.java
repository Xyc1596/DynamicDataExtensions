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
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashSet;
import java.util.Set;

public class AllStones extends Module {
    public AllStones() {
        super(DynamicDataMain.MOD_ID, "all_stones");
    }

    @Override
    public @NotNull Set<RecipeEntry> gatherRecipesToAdd() {
        final TagKey<Item> STONES = ResourceLocationUtils.createItemTagKey(
            ResourceLocationUtils.withCommonNamespace("stones")
        );
        final TagKey<Item> REDSTONE = ResourceLocationUtils.createItemTagKey(
            ResourceLocationUtils.withCommonNamespace("dusts/redstone")
        );
        final TagKey<Item> QUARTZ = ResourceLocationUtils.createItemTagKey(
            ResourceLocationUtils.withCommonNamespace("gems/quartz")
        );
        final TagKey<Item> IRON_INGOTS = ResourceLocationUtils.createItemTagKey(
            ResourceLocationUtils.withCommonNamespace("ingots/iron")
        );

        Set<RecipeEntry> output = new LinkedHashSet<>();

        output.add(
            RecipeUtils.createRecipeEntry(
                this,
                "repeater",
                ShapedRecipeBuilder
                    .shaped(RecipeCategory.REDSTONE, Items.REPEATER)
                    .define('S', STONES).define('R', REDSTONE).define('T', Items.REDSTONE_TORCH)
                    .pattern("TRT").pattern("SSS")
                    .unlockedBy(
                        "has_repeater_recipe",
                        CriterionUtils.recipeUnlocked("repeater")
                    )
            )
        );
        output.add(
            RecipeUtils.createRecipeEntry(
                this,
                "comparator",
                ShapedRecipeBuilder
                    .shaped(RecipeCategory.REDSTONE, Items.COMPARATOR)
                    .define('S', STONES).define('T', Items.REDSTONE_TORCH).define('Q', QUARTZ)
                    .pattern(" T ").pattern("TQT").pattern("SSS")
                    .unlockedBy(
                        "has_comparator_recipe",
                        CriterionUtils.recipeUnlocked("comparator")
                    )
            )
        );
        output.add(
            RecipeUtils.createRecipeEntry(
                this,
                "stonecutter",
                ShapedRecipeBuilder
                    .shaped(RecipeCategory.MISC, Items.STONECUTTER)
                    .define('S', STONES).define('I', IRON_INGOTS)
                    .pattern(" I ").pattern("SSS")
                    .unlockedBy(
                        "has_stonecutter_recipe",
                        CriterionUtils.recipeUnlocked("stonecutter")
                    )
            )
        );
        output.add(
            RecipeUtils.createRecipeEntry(
                this,
                "stone_pressure_plate",
                ShapedRecipeBuilder
                    .shaped(RecipeCategory.REDSTONE, Items.STONE_PRESSURE_PLATE)
                    .define('S', STONES)
                    .pattern("SS")
                    .unlockedBy(
                        "has_stone_pressure_plate_recipe",
                        CriterionUtils.recipeUnlocked("stone_pressure_plate_recipe")
                    )
            )
        );
        output.add(
            RecipeUtils.createRecipeEntry(
                this,
                "stone_button",
                ShapelessRecipeBuilder
                    .shapeless(RecipeCategory.REDSTONE, Items.STONE_BUTTON)
                    .requires(STONES)
                    .unlockedBy(
                        "has_stone_button_recipe",
                        CriterionUtils.recipeUnlocked("stone_button")
                    )
            )
        );

        return output;
    }

    @Override
    public @NotNull Set<ResourceLocation> gatherRecipesToRemove() {
        return Set.of(
            ResourceLocationUtils.withDefaultNamespace("repeater"),
            ResourceLocationUtils.withDefaultNamespace("comparator"),
            ResourceLocationUtils.withDefaultNamespace("stonecutter"),
            ResourceLocationUtils.withDefaultNamespace("stone_pressure_plate"),
            ResourceLocationUtils.withDefaultNamespace("stone_button")
        );
    }

    @Override
    protected @NotNull TranslatableLang buildOptionLang() {
        return this.getOptionLangBuilder()
                   .translation("zh_cn", "石材通用")
                   .translation("en_us", "All stones")
                   .build();
    }

    @Override
    protected @NotNull TranslatableLang buildTooltipLang() {
        return this
            .getTooltipLangBuilder()
            .translation(
                "zh_cn",
                "部分配方中的石头可以替换为标签 %s 包含的任何材料\n%s"
            )
            .translation(
                "en_us",
                "Stones / cobblestones in some recipes can be replaced with any material matching tag %s / %s\n%s"
            )
            .child(ModuleLangBuilder.literal("#c:stones").format(ChatFormatting.LIGHT_PURPLE).build())
            .child(
                ModuleLangBuilder
                    .translatable("tooltip", this.namespace, this.id + "_1")
                    .translation("zh_cn", "例：石头 -> 石头 / 深板岩 / 安山岩 / 闪长岩 / 花岗岩")
                    .translation("en_us", "E.g. stone -> stone / deepslate / andesite / diorite / granite")
                    .format(ChatFormatting.GRAY)
                    .build()
            )
            .build();
    }
}

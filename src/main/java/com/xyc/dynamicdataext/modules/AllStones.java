package com.xyc.dynamicdataext.modules;

import com.xyc.dynamicdataext.DynamicDataMain;
import com.xyc.dynamicdataext.base.DynamicRecipeEntry;
import com.xyc.dynamicdataext.base.Module;
import com.xyc.dynamicdataext.config.ModuleConfig;
import com.xyc.dynamicdataext.config.ModuleConfigBuilder;
import com.xyc.dynamicdataext.config.ModuleOptionBuilder;
import com.xyc.dynamicdataext.lang.ModuleLangBuilder;
import com.xyc.dynamicdataext.utils.ConfigUtils;
import com.xyc.dynamicdataext.utils.CriterionUtils;
import com.xyc.dynamicdataext.utils.LocationUtils;
import com.xyc.dynamicdataext.utils.RecipeUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashSet;
import java.util.Set;

public class AllStones extends Module {
    public static final String MODULE_ID = "all_stones";

    public AllStones() {
        super(DynamicDataMain.MOD_ID, MODULE_ID);
    }

    @Override
    public @NotNull Set<DynamicRecipeEntry> gatherRecipesToAdd() {
        Set<DynamicRecipeEntry> output = new LinkedHashSet<>();
        output.add(
            RecipeUtils.createRecipeEntry(
                this,
                "repeater",
                ShapedRecipeBuilder
                    .shaped(RecipeCategory.REDSTONE, Items.REPEATER)
                    .define('S', Tags.Items.STONES)
                    .define('R', Tags.Items.DUSTS_REDSTONE)
                    .define('T', Items.REDSTONE_TORCH)
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
                    .define('S', Tags.Items.STONES)
                    .define('T', Items.REDSTONE_TORCH)
                    .define('Q', Tags.Items.GEMS_QUARTZ)
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
                    .define('S', Tags.Items.STONES).define('I', Tags.Items.INGOTS_IRON)
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
                    .define('S', Tags.Items.STONES)
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
                    .requires(Tags.Items.STONES)
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
            LocationUtils.withDefaultNamespace("repeater"),
            LocationUtils.withDefaultNamespace("comparator"),
            LocationUtils.withDefaultNamespace("stonecutter"),
            LocationUtils.withDefaultNamespace("stone_pressure_plate"),
            LocationUtils.withDefaultNamespace("stone_button")
        );
    }

    @Override
    protected @NotNull ModuleConfig buildConfig() {
        ModuleConfigBuilder builder = this.createConfigBuilder();
        ModuleOptionBuilder<Boolean> enabled = ConfigUtils.createEnabledOptionBuilderWithTitle(builder);
        return builder.setTitle(builder
            .getTitleLangBuilder()
            .translation("zh_cn", "石材通用")
            .translation("en_us", "All stones")
            .build()
        ).defineEnabled(enabled
            .setDefaultValue(true)
            .setTooltip(enabled
                .getTooltipLangBuilder()
                .translation(
                    "zh_cn",
                    "部分配方中的石头可以替换为标签 %s 包含的任何材料\n%s"
                )
                .translation(
                    "en_us",
                    "Stones / cobblestones in some recipes can be replaced with any material matching " +
                        "tag %s / %s\n%s"
                )
                .child(ModuleLangBuilder.literal("#c:stones").format(ChatFormatting.LIGHT_PURPLE).build())
                .child(enabled
                    .createTooltipChildLangBuilder("1")
                    .translation("zh_cn", "例：石头 → 石头 / 深板岩 / 安山岩 / 闪长岩 / 花岗岩")
                    .translation("en_us", "E.g. stone → stone / deepslate / andesite / diorite / granite")
                    .format(ChatFormatting.GRAY)
                    .build()
                ).build()
            ).build()
        ).build();
    }
}

package com.xyc.dynamicdataext.modules;

import com.xyc.dynamicdataext.DynamicDataMain;
import com.xyc.dynamicdataext.base.Module;
import com.xyc.dynamicdataext.base.DynamicRecipeEntry;
import com.xyc.dynamicdataext.config.ModuleConfig;
import com.xyc.dynamicdataext.config.ModuleConfigBuilder;
import com.xyc.dynamicdataext.config.ModuleOptionBuilder;
import com.xyc.dynamicdataext.utils.ConfigUtils;
import com.xyc.dynamicdataext.utils.CriterionUtils;
import com.xyc.dynamicdataext.utils.RecipeUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashSet;
import java.util.Set;

public class ConvenientCrafting extends Module {
    public static final String MODULE_ID = "convenient_crafting";

    public ConvenientCrafting() {
        super(DynamicDataMain.MOD_ID, MODULE_ID);
    }

    @Override
    public @NotNull Set<DynamicRecipeEntry> gatherRecipesToAdd() {
        Set<DynamicRecipeEntry> output = new LinkedHashSet<>();

        output.add(
            RecipeUtils.createRecipeEntry(
                this,
                "chest_from_log",
                ShapedRecipeBuilder
                    .shaped(RecipeCategory.MISC, Items.CHEST, 4)
                    .define('L', ItemTags.LOGS)
                    .pattern("LLL").pattern("L L").pattern("LLL")
                    .unlockedBy(
                        "has_chest_recipe",
                        CriterionUtils.recipeUnlocked("chest")
                    )
            )
        );

        output.add(
            RecipeUtils.createRecipeEntry(
                this,
                "barrel_from_log",
                ShapedRecipeBuilder
                    .shaped(RecipeCategory.MISC, Items.BARREL, 4)
                    .define('L', ItemTags.LOGS)
                    .pattern("LLL").pattern("L L").pattern("L L")
                    .unlockedBy("has_barrel_recipe", CriterionUtils.recipeUnlocked("barrel"))
            )
        );

        final Criterion<RecipeUnlockedTrigger.TriggerInstance> CRITERION_LADDER =
            CriterionUtils.recipeUnlocked("ladder");
        output.add(
            RecipeUtils.createRecipeEntry(
                this,
                "ladder_from_log",
                ShapedRecipeBuilder
                    .shaped(RecipeCategory.MISC, Items.LADDER, 24)
                    .define('L', ItemTags.LOGS)
                    .pattern("L L").pattern("LLL").pattern("L L")
                    .unlockedBy("has_ladder_recipe", CRITERION_LADDER)
            )
        );
        output.add(
            RecipeUtils.createRecipeEntry(
                this,
                "ladder_from_plank",
                ShapedRecipeBuilder
                    .shaped(RecipeCategory.MISC, Items.LADDER, 6)
                    .define('P', ItemTags.PLANKS)
                    .pattern("P P").pattern("PPP").pattern("P P")
                    .unlockedBy("has_ladder_recipe", CRITERION_LADDER)
            )
        );

        output.add(
            RecipeUtils.createRecipeEntry(
                this,
                "hopper_from_log",
                ShapedRecipeBuilder
                    .shaped(RecipeCategory.REDSTONE, Items.HOPPER)
                    .define('L', ItemTags.LOGS).define('I', Tags.Items.INGOTS_IRON)
                    .pattern("ILI").pattern("ILI").pattern(" I ")
                    .unlockedBy("has_hopper_recipe", CriterionUtils.recipeUnlocked("hopper"))
            )
        );

        output.add(
            RecipeUtils.createRecipeEntry(
                this,
                "dispenser_from_dropper",
                ShapelessRecipeBuilder
                    .shapeless(RecipeCategory.REDSTONE, Items.DISPENSER)
                    .requires(Items.DROPPER).requires(Tags.Items.TOOLS_BOW)
                    .unlockedBy("has_dispenser_recipe", CriterionUtils.recipeUnlocked("dispenser"))
            )
        );

        output.add(
            RecipeUtils.createRecipeEntry(
                this,
                "chain_from_iron_block",
                ShapedRecipeBuilder
                    .shaped(RecipeCategory.MISC, Items.CHAIN, 9)
                    .define('I', Tags.Items.INGOTS_IRON).define('B', Tags.Items.STORAGE_BLOCKS_IRON)
                    .pattern("I").pattern("B").pattern("I")
                    .unlockedBy("has_chain_recipe", CriterionUtils.recipeUnlocked("chain"))
            )
        );

        final Criterion<RecipeUnlockedTrigger.TriggerInstance> CRITERION_REPEATER =
            CriterionUtils.recipeUnlocked("repeater");
        if (DynamicDataMain.CONFIG.isModuleEnabled("all_stones")) {
            output.add(
                RecipeUtils.createRecipeEntry(
                    this,
                    "repeater_from_redstone",
                    ShapedRecipeBuilder
                        .shaped(RecipeCategory.REDSTONE, Items.REPEATER)
                        .define('S', Tags.Items.STONES)
                        .define('R', Tags.Items.DUSTS_REDSTONE)
                        .define('r', Tags.Items.RODS_WOODEN)
                        .pattern("R R").pattern("rRr").pattern("SSS")
                        .unlockedBy("has_repeater_recipe", CRITERION_REPEATER)
                )
            );
        } else {
            output.add(
                RecipeUtils.createRecipeEntry(
                    this,
                    "repeater_from_redstone",
                    ShapedRecipeBuilder
                        .shaped(RecipeCategory.REDSTONE, Items.REPEATER)
                        .define('S', Items.STONE)
                        .define('R', Tags.Items.DUSTS_REDSTONE)
                        .define('r', Tags.Items.RODS_WOODEN)
                        .pattern("R R").pattern("rRr").pattern("SSS")
                        .unlockedBy("has_repeater_recipe", CRITERION_REPEATER)
                )
            );
        }

        return output;
    }

    @Override
    protected @NotNull ModuleConfig buildConfig() {
        ModuleConfigBuilder builder = this.createConfigBuilder();
        ModuleOptionBuilder<Boolean> enabled = ConfigUtils.createEnabledOptionBuilderWithTitle(builder);
        return builder.setTitle(builder
            .getTitleLangBuilder()
            .translation("zh_cn", "便捷合成")
            .translation("en_us", "Convenient Crafting")
            .build()
        ).defineEnabled(enabled
            .setDefaultValue(true)
            .setTooltip(enabled
                .getTooltipLangBuilder()
                .translation(
                    "zh_cn",
                    "省略部分中间产物 / 添加包含中间产物的合成路线\n%s\n%s"
                ).translation(
                    "en_us",
                    "Omit some intermediate products / add routes with intermediate products\n%s\n%s"
                ).child(enabled
                    .createTooltipChildLangBuilder()
                    .translation("zh_cn", "灵感来源：Quark")
                    .translation("en_us", "Inspired by: Quark")
                    .format(ChatFormatting.ITALIC)
                    .build()
                ).child(enabled
                    .createTooltipChildLangBuilder()
                    .translation("zh_cn", "例：8 原木 → 4 箱子，投掷器 + 弓 → 发射器")
                    .translation("en_us", "E.g. 8 Logs → 4 chests, dropper + bow → dispenser")
                    .format(ChatFormatting.GRAY)
                    .build()
                ).build()
            ).build()
        ).build();
    }
}

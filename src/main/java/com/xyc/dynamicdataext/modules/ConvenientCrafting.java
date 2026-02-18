package com.xyc.dynamicdataext.modules;

import com.xyc.dynamicdataext.DynamicDataMain;
import com.xyc.dynamicdataext.base.Module;
import com.xyc.dynamicdataext.base.RecipeEntry;
import com.xyc.dynamicdataext.config.ModuleConfig;
import com.xyc.dynamicdataext.config.ModuleConfigBuilder;
import com.xyc.dynamicdataext.config.ModuleOptionBuilder;
import com.xyc.dynamicdataext.utils.CriterionUtils;
import com.xyc.dynamicdataext.utils.RecipeUtils;
import com.xyc.dynamicdataext.utils.ResourceLocationUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashSet;
import java.util.Set;

public class ConvenientCrafting extends Module {
    public ConvenientCrafting() {
        super(DynamicDataMain.MOD_ID, "convenient_crafting");
    }

    @Override
    public @NotNull Set<RecipeEntry> gatherRecipesToAdd() {
        final TagKey<Item> LOGS = ResourceLocationUtils.createItemTagKey(
            ResourceLocationUtils.withDefaultNamespace("logs")
        );
        final TagKey<Item> PLANKS = ResourceLocationUtils.createItemTagKey(
            ResourceLocationUtils.withDefaultNamespace("planks")
        );
        final TagKey<Item> WOODEN_RODS = ResourceLocationUtils.createItemTagKey(
            ResourceLocationUtils.withCommonNamespace("rods/wooden")
        );
        final TagKey<Item> IRON_INGOTS = ResourceLocationUtils.createItemTagKey(
            ResourceLocationUtils.withCommonNamespace("ingots/iron")
        );
        final TagKey<Item> IRON_BLOCKS = ResourceLocationUtils.createItemTagKey(
            ResourceLocationUtils.withCommonNamespace("storage_blocks/iron")
        );
        final TagKey<Item> STONES = ResourceLocationUtils.createItemTagKey(
            ResourceLocationUtils.withCommonNamespace("stones")
        );
        final TagKey<Item> REDSTONE = ResourceLocationUtils.createItemTagKey(
            ResourceLocationUtils.withCommonNamespace("dusts/redstone")
        );
        final TagKey<Item> BOWS = ResourceLocationUtils.createItemTagKey(
            ResourceLocationUtils.withCommonNamespace("tools/bow")
        );

        Set<RecipeEntry> output = new LinkedHashSet<>();

        output.add(
            RecipeUtils.createRecipeEntry(
                this,
                "chest_from_log",
                ShapedRecipeBuilder
                    .shaped(RecipeCategory.MISC, Items.CHEST, 4)
                    .define('L', LOGS)
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
                    .define('L', LOGS)
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
                    .define('L', LOGS)
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
                    .define('P', PLANKS)
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
                    .define('L', LOGS).define('I', IRON_INGOTS)
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
                    .requires(Items.DROPPER).requires(BOWS)
                    .unlockedBy("has_dispenser_recipe", CriterionUtils.recipeUnlocked("dispenser"))
            )
        );

        output.add(
            RecipeUtils.createRecipeEntry(
                this,
                "chain_from_iron_block",
                ShapedRecipeBuilder
                    .shaped(RecipeCategory.MISC, Items.CHAIN, 9)
                    .define('I', IRON_INGOTS).define('B', IRON_BLOCKS)
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
                        .define('S', STONES).define('R', REDSTONE).define('r', WOODEN_RODS)
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
                        .define('S', Items.STONE).define('R', REDSTONE).define('r', WOODEN_RODS)
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
        ModuleOptionBuilder<Boolean> enabled = builder.createEnabledOptionBuilderWithDefaultTitle();
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
                    .createTooltipChildLangBuilder("1")
                    .translation("zh_cn", "灵感来源：Quark")
                    .translation("en_us", "Inspired by: Quark")
                    .format(ChatFormatting.ITALIC)
                    .build()
                ).child(enabled
                    .createTooltipChildLangBuilder("2")
                    .translation("zh_cn", "例：8 原木 -> 4 箱子，投掷器 + 弓 -> 发射器")
                    .translation("en_us", "E.g. 8 Logs -> 4 chests, dropper + bow -> dispenser")
                    .format(ChatFormatting.GRAY)
                    .build()
                ).build()
            ).build()
        ).build();
    }
}

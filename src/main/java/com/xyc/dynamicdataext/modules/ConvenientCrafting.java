package com.xyc.dynamicdataext.modules;

import com.xyc.dynamicdataext.ModMain;
import com.xyc.dynamicdataext.utils.CriterionUtils;
import com.xyc.dynamicdataext.base.Module;
import com.xyc.dynamicdataext.utils.RecipeUtils;
import com.xyc.dynamicdataext.lang.ModuleLangBuilder;
import com.xyc.dynamicdataext.lang.TranslatableLang;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class ConvenientCrafting extends Module {
    public ConvenientCrafting() {
        super(ModMain.MOD_ID, "convenient_crafting");
    }

    @Override
    public @NotNull Set<RecipeHolder<Recipe<?>>> gatherRecipesToAdd() {
        final TagKey<Item>
            LOGS = RecipeUtils.createItemTagKey(ResourceLocation.withDefaultNamespace("logs")),
            PLANKS = RecipeUtils.createItemTagKey(ResourceLocation.withDefaultNamespace("planks")),
            WOODEN_RODS = RecipeUtils.createItemTagKey(RecipeUtils.withCommonNamespace("rods/wooden")),
            IRON_INGOTS = RecipeUtils.createItemTagKey(RecipeUtils.withCommonNamespace("ingots/iron")),
            IRON_BLOCKS = RecipeUtils.createItemTagKey(RecipeUtils.withCommonNamespace("storage_blocks/iron")),
            STONES = RecipeUtils.createItemTagKey(RecipeUtils.withCommonNamespace("stones")),
            REDSTONE = RecipeUtils.createItemTagKey(RecipeUtils.withCommonNamespace("dusts/redstone")),
            BOWS = RecipeUtils.createItemTagKey(RecipeUtils.withCommonNamespace("tools/bow"));

        Set<RecipeHolder<Recipe<?>>> output = new HashSet<>();

        output.add(
            RecipeUtils.createRecipeHolder(
                this.namespace,
                "chest_from_log",
                ShapedRecipeBuilder
                    .shaped(RecipeCategory.MISC, Items.CHEST, 4)
                    .define('L', LOGS)
                    .pattern("LLL").pattern("L L").pattern("LLL")
                    .unlockedBy("has_chest_recipe", CriterionUtils.recipeUnlocked("chest"))
            )
        );

        output.add(
            RecipeUtils.createRecipeHolder(
                this.namespace,
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
            RecipeUtils.createRecipeHolder(
                this.namespace,
                "ladder_from_log",
                ShapedRecipeBuilder
                    .shaped(RecipeCategory.MISC, Items.LADDER, 24)
                    .define('L', LOGS)
                    .pattern("L L").pattern("LLL").pattern("L L")
                    .unlockedBy("has_ladder_recipe", CRITERION_LADDER)
            )
        );
        output.add(
            RecipeUtils.createRecipeHolder(
                this.namespace,
                "ladder_from_plank",
                ShapedRecipeBuilder
                    .shaped(RecipeCategory.MISC, Items.LADDER, 6)
                    .define('P', PLANKS)
                    .pattern("P P").pattern("PPP").pattern("P P")
                    .unlockedBy("has_ladder_recipe", CRITERION_LADDER)
            )
        );

        output.add(
            RecipeUtils.createRecipeHolder(
                this.namespace,
                "hopper_from_log",
                ShapedRecipeBuilder
                    .shaped(RecipeCategory.REDSTONE, Items.HOPPER)
                    .define('L', LOGS).define('I', IRON_INGOTS)
                    .pattern("ILI").pattern("ILI").pattern(" I ")
                    .unlockedBy("has_hopper_recipe", CriterionUtils.recipeUnlocked("hopper"))
            )
        );

        output.add(
            RecipeUtils.createRecipeHolder(
                this.namespace,
                "dispenser_from_dropper",
                ShapelessRecipeBuilder
                    .shapeless(RecipeCategory.REDSTONE, Items.DISPENSER)
                    .requires(Items.DROPPER).requires(BOWS)
                    .unlockedBy("has_dispenser_recipe", CriterionUtils.recipeUnlocked("dispenser"))
            )
        );

        output.add(
            RecipeUtils.createRecipeHolder(
                this.namespace,
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
        if (ModMain.CONFIG.isModuleEnabled("all_stones")) {
            output.add(
                RecipeUtils.createRecipeHolder(
                    this.namespace,
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
                RecipeUtils.createRecipeHolder(
                    this.namespace,
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
    protected @NotNull TranslatableLang buildOptionLang() {
        return ModuleLangBuilder.translatable("option", this.namespace, this.id)
                                .translation("zh_cn", "便捷合成")
                                .translation("en_us", "Convenient Crafting")
                                .build();
    }

    @Override
    protected @NotNull TranslatableLang buildTooltipLang() {
        return ModuleLangBuilder
            .translatable("tooltip", this.namespace, this.id)
            .translation(
                "zh_cn",
                "省略部分中间产物 / 添加包含中间产物的合成路线\n%s\n%s"
            )
            .translation(
                "en_us",
                "Omit some intermediate products / add routes with intermediate products\n%s\n%s"
            )
            .child(
                ModuleLangBuilder
                    .translatable("tooltip", this.namespace, this.id + "_1")
                    .translation("zh_cn", "灵感来源：Quark")
                    .translation("en_us", "Inspired by: Quark")
                    .format(ChatFormatting.ITALIC)
                    .build()
            )
            .child(
                ModuleLangBuilder
                    .translatable("tooltip", this.namespace, this.id + "_2")
                    .translation("zh_cn", "例：8 原木 -> 4 箱子，投掷器 + 弓 -> 发射器")
                    .translation("en_us", "E.g. 8 Logs -> 4 chests, dropper + bow -> dispenser")
                    .format(ChatFormatting.GRAY)
                    .build()
            )
            .build();
    }
}

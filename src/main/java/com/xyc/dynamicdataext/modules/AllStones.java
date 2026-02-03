package com.xyc.dynamicdataext.modules;

import com.xyc.dynamicdataext.ModMain;
import com.xyc.dynamicdataext.base.Module;
import com.xyc.dynamicdataext.lang.ModuleLangBuilder;
import com.xyc.dynamicdataext.lang.TranslatableLang;
import com.xyc.dynamicdataext.utils.CriterionUtils;
import com.xyc.dynamicdataext.utils.RecipeUtils;
import net.minecraft.ChatFormatting;
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

public class AllStones extends Module {
    public AllStones() {
        super(ModMain.MOD_ID, "all_stones");
    }

    @Override
    public @NotNull Set<RecipeHolder<Recipe<?>>> gatherRecipesToAdd() {
        final TagKey<Item>
            STONES = RecipeUtils.createItemTagKey(RecipeUtils.withCommonNamespace("stones")),
            REDSTONE = RecipeUtils.createItemTagKey(RecipeUtils.withCommonNamespace("dusts/redstone")),
            QUARTZ = RecipeUtils.createItemTagKey(RecipeUtils.withCommonNamespace("gems/quartz")),
            IRON_INGOTS = RecipeUtils.createItemTagKey(RecipeUtils.withCommonNamespace("ingots/iron"));

        Set<RecipeHolder<Recipe<?>>> output = new HashSet<>();

        output.add(
            RecipeUtils.createRecipeHolder(
                this.namespace,
                "repeater",
                ShapedRecipeBuilder
                    .shaped(RecipeCategory.REDSTONE, Items.REPEATER)
                    .define('S', STONES).define('R', REDSTONE).define('T', Items.REDSTONE_TORCH)
                    .pattern("TRT").pattern("SSS")
                    .unlockedBy("has_repeater_recipe", CriterionUtils.recipeUnlocked("repeater"))
            )
        );
        output.add(
            RecipeUtils.createRecipeHolder(
                this.namespace,
                "comparator",
                ShapedRecipeBuilder
                    .shaped(RecipeCategory.REDSTONE, Items.COMPARATOR)
                    .define('S', STONES).define('T', Items.REDSTONE_TORCH).define('Q', QUARTZ)
                    .pattern(" T ").pattern("TQT").pattern("SSS")
                    .unlockedBy("has_comparator_recipe", CriterionUtils.recipeUnlocked("comparator"))
            )
        );
        output.add(
            RecipeUtils.createRecipeHolder(
                this.namespace,
                "stonecutter",
                ShapedRecipeBuilder
                    .shaped(RecipeCategory.MISC, Items.STONECUTTER)
                    .define('S', STONES).define('I', IRON_INGOTS)
                    .pattern(" I ").pattern("SSS")
                    .unlockedBy("has_stonecutter_recipe", CriterionUtils.recipeUnlocked("stonecutter"))
            )
        );
        output.add(
            RecipeUtils.createRecipeHolder(
                this.namespace,
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
            RecipeUtils.createRecipeHolder(
                this.namespace,
                "stone_button",
                ShapelessRecipeBuilder
                    .shapeless(RecipeCategory.REDSTONE, Items.STONE_BUTTON)
                    .requires(STONES)
                    .unlockedBy("has_stone_button_recipe", CriterionUtils.recipeUnlocked("stone_button"))
            )
        );

        return output;
    }

    @Override
    public @NotNull Set<ResourceLocation> gatherRecipesToRemove() {
        return Set.of(
            ResourceLocation.withDefaultNamespace("repeater"),
            ResourceLocation.withDefaultNamespace("comparator"),
            ResourceLocation.withDefaultNamespace("stonecutter"),
            ResourceLocation.withDefaultNamespace("stone_pressure_plate"),
            ResourceLocation.withDefaultNamespace("stone_button")
        );
    }

    @Override
    protected @NotNull TranslatableLang buildOptionLang() {
        return ModuleLangBuilder
            .translatable("option", this.namespace, this.id)
            .translation("zh_cn", "石材通用")
            .translation("en_us", "All stones")
            .build();
    }

    @Override
    protected @NotNull TranslatableLang buildTooltipLang() {
        return ModuleLangBuilder
            .translatable("tooltip", this.namespace, this.id)
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

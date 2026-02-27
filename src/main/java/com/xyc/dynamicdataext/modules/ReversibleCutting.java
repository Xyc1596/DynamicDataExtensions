package com.xyc.dynamicdataext.modules;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.xyc.dynamicdataext.DynamicDataMain;
import com.xyc.dynamicdataext.DynamicDataRegistry;
import com.xyc.dynamicdataext.base.Module;
import com.xyc.dynamicdataext.base.*;
import com.xyc.dynamicdataext.config.ModuleConfig;
import com.xyc.dynamicdataext.config.ModuleConfigBuilder;
import com.xyc.dynamicdataext.config.ModuleOption;
import com.xyc.dynamicdataext.config.ModuleOptionBuilder;
import com.xyc.dynamicdataext.lang.ModuleLangBuilder;
import com.xyc.dynamicdataext.lang.TranslatableBuilder;
import com.xyc.dynamicdataext.utils.*;
import net.minecraft.ChatFormatting;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.StonecutterRecipe;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ReversibleCutting extends Module {
    private final Multimap<Item, Item> mapMaterialToProduction = LinkedHashMultimap.create();
    private final Multimap<Item, Item> mapProductionFromMaterial = LinkedHashMultimap.create();
    private final DirectedGraph<Item> graph = new DirectedGraph<>();

    protected ModuleOption<Boolean>
        reversionStoneBricks;

    public static final TagKey<Item>
        STONE_BRICKS = cuttingItemTag("stone_bricks"),
        STONE = cuttingItemTag("stone"),
        COBBLESTONE = cuttingItemTag("cobblestone"),
        MOSSY_COBBLESTONE = cuttingItemTag("mossy_cobblestone"),
        MOSSY_STONE_BRICKS = cuttingItemTag("mossy_stone_bricks"),
        SMOOTH_STONE = cuttingItemTag("smooth_stone"),
        GRANITE = cuttingItemTag("granite"),
        POLISHED_GRANITE = cuttingItemTag("polished_granite"),
        DIORITE = cuttingItemTag("diorite"),
        POLISHED_DIORITE = cuttingItemTag("polished_diorite"),
        ANDESITE = cuttingItemTag("andesite"),
        POLISHED_ANDESITE = cuttingItemTag("polished_andesite"),
        SANDSTONE = cuttingItemTag("sandstone");

    public ReversibleCutting() {
        super(DynamicDataMain.MOD_ID, "reversible_cutting");
    }

    @Override
    public @NotNull Set<DynamicTagEntry<?>> gatherTagsToAdd() {
        Set<DynamicTagEntry<?>> output = new LinkedHashSet<>();

        Item[] stoneBricks = {
            Items.STONE_BRICKS, Items.STONE_STAIRS, Items.STONE_BRICK_WALL, Items.CHISELED_STONE_BRICKS
        };
        Item[] smoothStone = {Items.SMOOTH_STONE};
        Item[] stone = {Items.STONE, Items.STONE_STAIRS};
        if (this.reversionStoneBricks.getValue())
            stone = ArrayUtils.addAll(stone, stoneBricks);

        Item[] cobblestone = {Items.COBBLESTONE, Items.COBBLESTONE_STAIRS, Items.COBBLESTONE_WALL};
        Item[] mossyCobblestone = {
            Items.MOSSY_COBBLESTONE, Items.MOSSY_COBBLESTONE_STAIRS, Items.MOSSY_COBBLESTONE_WALL
        };
        Item[] mossyStoneBricks = {
            Items.MOSSY_STONE_BRICKS, Items.MOSSY_STONE_BRICK_STAIRS, Items.MOSSY_STONE_BRICK_WALL
        };

        Item[] polished_granite = {Items.POLISHED_GRANITE, Items.POLISHED_GRANITE_STAIRS};
        Item[] granite = {Items.GRANITE, Items.GRANITE_STAIRS, Items.GRANITE_WALL};
        Item[] polished_diorite = {Items.POLISHED_DIORITE, Items.POLISHED_DIORITE_STAIRS};
        Item[] diorite = {Items.DIORITE, Items.DIORITE_STAIRS, Items.DIORITE_WALL};
        Item[] polished_andesite = {Items.POLISHED_ANDESITE, Items.POLISHED_ANDESITE_STAIRS};
        Item[] andesite = {Items.ANDESITE, Items.ANDESITE_STAIRS, Items.ANDESITE_WALL};
        granite = ArrayUtils.addAll(granite, polished_granite);
        diorite = ArrayUtils.addAll(diorite, polished_diorite);
        andesite = ArrayUtils.addAll(andesite, polished_andesite);

        Item[] sandstone = {
            Items.SANDSTONE, Items.SANDSTONE_STAIRS, Items.SANDSTONE_WALL, Items.CUT_SANDSTONE,
            Items.CHISELED_SANDSTONE
        };

        output.add(TagUtils.createItemTagEntry(STONE, stone));
        output.add(TagUtils.createItemTagEntry(COBBLESTONE, cobblestone));
        output.add(TagUtils.createItemTagEntry(MOSSY_COBBLESTONE, mossyCobblestone));
        output.add(TagUtils.createItemTagEntry(STONE_BRICKS, stoneBricks));
        output.add(TagUtils.createItemTagEntry(MOSSY_STONE_BRICKS, mossyStoneBricks));
        output.add(TagUtils.createItemTagEntry(SMOOTH_STONE, smoothStone));
        output.add(TagUtils.createItemTagEntry(GRANITE, granite));
        output.add(TagUtils.createItemTagEntry(POLISHED_GRANITE, polished_granite));
        output.add(TagUtils.createItemTagEntry(DIORITE, diorite));
        output.add(TagUtils.createItemTagEntry(POLISHED_DIORITE, polished_diorite));
        output.add(TagUtils.createItemTagEntry(ANDESITE, andesite));
        output.add(TagUtils.createItemTagEntry(POLISHED_ANDESITE, polished_andesite));
        output.add(TagUtils.createItemTagEntry(SANDSTONE, sandstone));

        return Set.of();
    }

    @Override
    public @Nullable Predicate<Recipe<?>> gatherRecipeExcluder() {
        IRecipeManagerExtensions manager = DynamicDataRegistry.getRecipeManager();
        this.graph.clear();
        return recipe -> {
            if (recipe instanceof StonecutterRecipe r) {
                ItemStack resultStack = manager.getResultItemStack(r);
                if (resultStack.getCount() == 1) {
                    Item resultItem = resultStack.getItem();
                    for (Ingredient ingredient : r.getIngredients())
                        for (ItemStack itemStack : ingredient.getItems()) {
                            this.graph.addEdge(itemStack.getItem(), resultItem);
                        }
                    return true;
                }
            }
            return false;
        };
    }

    @Override
    public @Nullable Supplier<Set<DynamicRecipeEntry>> gatherRecipeProvider() {
        return () -> {
            // Map<String, DynamicRecipeEntry> output = new LinkedHashMap<>();
            Set<DynamicRecipeEntry> output = new LinkedHashSet<>();
            for (Map.Entry<Item, DirectedGraph.Node<Item>> entry : this.graph) {
                Item material = entry.getKey();
                DirectedGraph.Node<Item> node = entry.getValue();
                Set<Item> children = node.getChildren();
                Set<Item> parents = node.getParents();
                Set<Item> ingredientSet = new LinkedHashSet<>(parents);
                if (children.isEmpty()) {   // 末端：原材料为上一级和同级任意物品
                    for (Item parent : parents) {
                        if (this.graph.maxDistance(material, parent) == 1) {
                            for (Item parentChild : this.graph.getChildren(parent)) {
                                if (!parentChild.equals(material))
                                    ingredientSet.add(parentChild);
                            }
                        }
                    }
                } else {    // 非末端：原材料为上一级和下一级任意物品
                    ingredientSet.addAll(children);
                }

                Item[] ingredients = ingredientSet.toArray(new Item[0]);
                output.add(RecipeUtils.createRecipeEntry(
                    this,
                    LocationUtils.getItemId(material) + "_cutting",
                    SingleItemRecipeBuilder.stonecutting(
                        Ingredient.of(ingredients),
                        RecipeCategory.BUILDING_BLOCKS,
                        material
                    ).unlockedBy("has_material", CriterionUtils.hasItems(ingredients))
                ));
            }

            return output;


            // Set<Map.Entry<Item, Collection<Item>>> entries = Sets.union(
            //     mapMaterialToProduction.asMap().entrySet(), mapProductionFromMaterial.asMap().entrySet()
            // );
            //
            // for (Map.Entry<Item, Collection<Item>> entry : entries) {
            //     Item result = entry.getKey();
            //     String recipeId = LocationUtils.getItemId(result);
            //     if (output.containsKey(recipeId))
            //         continue;
            //     Item[] ingredients = entry.getValue().toArray(new Item[0]);
            //     output.put(
            //         recipeId,
            //         RecipeUtils.createRecipeEntry(
            //             this,
            //             recipeId,
            //             SingleItemRecipeBuilder.stonecutting(
            //                 Ingredient.of(ingredients),
            //                 RecipeCategory.BUILDING_BLOCKS,
            //                 result
            //             ).unlockedBy("has_material", CriterionUtils.hasItems(ingredients))
            //         ));
            // }
            // return new LinkedHashSet<>(output.values());
        };
    }

    @Override
    protected @NotNull ModuleConfig buildConfig() {
        ModuleConfigBuilder builder = this.createConfigBuilder();
        ModuleOptionBuilder<Boolean> enabled = ConfigUtils.createEnabledOptionBuilderWithTitle(builder);

        this.reversionStoneBricks = this.reversionOption(
            builder, "stone_bricks", true,
            "石砖", "Stone Bricks",
            "石砖", "Stone brick",
            "石头", "stone"
        );

        builder.setTitle(builder
            .getTitleLangBuilder()
            .translation("zh_cn", "可逆切石")
            .translation("en_us", "Reversible Cutting")
            .build()
        ).defineEnabled(enabled
            .setTooltip(enabled
                .getTooltipLangBuilder()
                .translation(
                    "zh_cn",
                    "部分切石配方的材料和产物可以任意相互转换\n%s"
                ).translation(
                    "en_us",
                    "The ingredients and products in some stonecutting recipes can be mutually " +
                        "converted into each other arbitrarily\n%s"
                ).child(enabled
                    .createTooltipChildLangBuilder()
                    .translation("zh_cn", "仅对原版物品有效")
                    .translation("en_us", "Only effective for vanilla items")
                    .format(ChatFormatting.ITALIC)
                    .build()
                ).build()
            ).build()
        ).defineOption(this.reversionStoneBricks);

        return builder.build();
    }

    private static TagKey<Item> cuttingItemTag(String itemId) {
        return TagUtils.createItemTag(DynamicDataMain.MOD_ID, "cutting." + itemId);
    }

    protected ModuleOption<Boolean> reversionOption(
        ModuleConfigBuilder builder,
        String optionId,
        boolean defaultValue,
        String titleChild_ZH,
        String titleChild_EN,
        String tooltipChild1_ZH,
        String tooltipChild1_EN,
        String tooltipChild2_ZH,
        String tooltipChild2_EN
    ) {
        ModuleOptionBuilder<Boolean> optionBuilder = builder.createBooleanOptionBuilder("reversion." + optionId);
        return optionBuilder
            .setTitle(this
                .reversionOptionTitleBuilder()
                .child(this
                    .reversionOptionTitleChildBuilder(optionId)
                    .translation("zh_cn", titleChild_ZH)
                    .translation("en_us", titleChild_EN)
                    .build()
                ).build()
            ).setTooltip(this
                .reversionOptionTooltipBuilder()
                .child(this
                    .reversionOptionTooltipChildBuilder(optionId, "material1")
                    .translation("zh_cn", tooltipChild1_ZH)
                    .translation("en_us", tooltipChild1_EN)
                    .build()
                ).child(this
                    .reversionOptionTooltipChildBuilder(optionId, "material2")
                    .translation("zh_cn", tooltipChild2_ZH)
                    .translation("en_us", tooltipChild2_EN)
                    .build()
                ).build()
            ).setDefaultValue(defaultValue).build();
    }

    protected TranslatableBuilder reversionOptionTitleBuilder() {
        return ModuleLangBuilder
            .translatable(true, "module", this.namespace, "reversion", "title")
            .translation("zh_cn", "材料退化 - %s")
            .translation("en_us", "Material Reversion - %s");
    }

    protected TranslatableBuilder reversionOptionTitleChildBuilder(String optionId) {
        return ModuleLangBuilder
            .translatable("module", this.namespace, "reversion", optionId, "title", "material");
    }

    protected TranslatableBuilder reversionOptionTooltipBuilder() {
        return ModuleLangBuilder
            .translatable(true, "module", this.namespace, "reversion", "tooltip")
            .translation("zh_cn", "%s类方块可转化为%s类方块")
            .translation("en_us", "%s-type blocks can be converted into %s-type blocks");
    }

    protected TranslatableBuilder reversionOptionTooltipChildBuilder(String optionId, String childId) {
        return ModuleLangBuilder
            .translatable("module", this.namespace, "reversion", optionId, "tooltip", childId);
    }
}

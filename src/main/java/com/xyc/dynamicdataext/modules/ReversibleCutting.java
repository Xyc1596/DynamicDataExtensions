package com.xyc.dynamicdataext.modules;

import com.xyc.dynamicdataext.DynamicDataMain;
import com.xyc.dynamicdataext.DynamicDataRegistry;
import com.xyc.dynamicdataext.base.Module;
import com.xyc.dynamicdataext.base.*;
import com.xyc.dynamicdataext.config.ModuleConfig;
import com.xyc.dynamicdataext.config.ModuleConfigBuilder;
import com.xyc.dynamicdataext.config.ModuleOption;
import com.xyc.dynamicdataext.config.ModuleOptionBuilder;
import com.xyc.dynamicdataext.lang.PlaceholderLang;
import com.xyc.dynamicdataext.lang.TranslatableLang;
import com.xyc.dynamicdataext.utils.*;
import net.minecraft.ChatFormatting;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.StonecutterRecipe;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Supplier;

public class ReversibleCutting extends Module {
    private final SingleItemRecipeGraph graph = new SingleItemRecipeGraph();
    protected ModuleOption<List<String>> recipeList;
    protected ModuleOption<ListMode> recipeListMode;

    public ReversibleCutting() {
        super(DynamicDataMain.MOD_ID, "reversible_cutting");
    }

    @Override
    public BiPredicate<ResourceLocation, Recipe<?>> gatherRecipeExcluder() {
        IRecipeManagerExtensions manager = DynamicDataRegistry.getRecipeManager();
        this.graph.clear();
        SingleIDCollection collection = SingleIDCollection.of(this.recipeList.getValue());
        boolean whitelist = this.recipeListMode.getValue() == ListMode.WHITELIST;
        return (location, recipe) -> {
            if (recipe instanceof StonecutterRecipe r && collection.test(location, whitelist)) {
                ItemStack resultStack = manager.getResultItemStack(r);
                for (Ingredient ingredient : r.getIngredients())
                    for (ItemStack itemStack : ingredient.getItems())
                        this.graph.addItem(resultStack, itemStack.getItem());
                return true;
            }
            return false;
        };
    }

    @Override
    public @Nullable Supplier<Set<DynamicRecipeEntry>> gatherRecipeProvider() {
        return () -> {
            Set<DynamicRecipeEntry> output = new LinkedHashSet<>();
            for (Map.Entry<Item, Map<Item, Integer>> group : this.graph.getAllGroups().entrySet()) {
                Map<Item, Integer> itemMap = group.getValue();
                Integer[] counts = new TreeSet<>(itemMap.values()).toArray(new Integer[0]);
                Map<Integer, List<Item>> countToItemList = new HashMap<>();
                Map<Integer, Item[]> countToItemArray = new HashMap<>();
                for (Map.Entry<Item, Integer> entry : itemMap.entrySet())
                    countToItemList.computeIfAbsent(entry.getValue(), c -> new LinkedList<>()).add(entry.getKey());
                for (Map.Entry<Integer, List<Item>> entry : countToItemList.entrySet())
                    countToItemArray.put(entry.getKey(), entry.getValue().toArray(new Item[0]));

                for (int resultLayerIdx = 0; resultLayerIdx < counts.length; resultLayerIdx++) {
                    int resultLayerCount = counts[resultLayerIdx];
                    List<Item> resultLayerItems = countToItemList.get(resultLayerCount);
                    int resultLayerItemCount = resultLayerItems.size();

                    // 获取比当前物品count更小的count值
                    Integer[] upperLayerCounts = ArrayUtils.subarray(counts, 0, resultLayerIdx);

                    for (Item result : resultLayerItems) {
                        // 同层物品合成
                        Item[] ingredients = new Item[resultLayerItemCount - 1];
                        int ingredientIdx = 0;
                        for (Item ingredientItem : resultLayerItems)
                            if (!ingredientItem.equals(result))
                                ingredients[ingredientIdx++] = ingredientItem;
                        String recipeId = LocationUtils.getItemId(result) + "_cutting";
                        output.add(RecipeUtils.createRecipeEntry(
                            this,
                            recipeId,
                            SingleItemRecipeBuilder.stonecutting(
                                Ingredient.of(ingredients),
                                RecipeCategory.BUILDING_BLOCKS,
                                result,
                                1
                            ).unlockedBy("has_materials", CriterionUtils.hasItems(ingredients))
                        ));

                        // count更小的层合成
                        for (int upperLayerCount : upperLayerCounts) {
                            Item[] upperLayerIngredients = countToItemArray.get(upperLayerCount);
                            if (resultLayerCount % upperLayerCount > 0)
                                continue;
                            int resultCount = resultLayerCount / upperLayerCount;
                            String upperLayerRecipeId = recipeId + "_" + resultCount;
                            output.add(RecipeUtils.createRecipeEntry(
                                this,
                                upperLayerRecipeId,
                                SingleItemRecipeBuilder.stonecutting(
                                    Ingredient.of(upperLayerIngredients),
                                    RecipeCategory.BUILDING_BLOCKS,
                                    result,
                                    resultCount
                                ).unlockedBy("has_materials", CriterionUtils.hasItems(upperLayerIngredients))
                            ));
                        }
                    }
                }
            }
            return output;
        };
    }

    @Override
    protected @NotNull ModuleConfig buildConfig() {
        ModuleConfigBuilder builder = this.createConfigBuilder();
        ModuleOptionBuilder<Boolean> enabled = ConfigUtils.createEnabledOptionBuilderWithTitle(builder);

        ModuleOptionBuilder<List<String>> recipeList = builder
            .createStringListOptionBuilder("recipe_list")
            .setDefaultValue(List.of("@create:.*"))
            .setTooltip(ConfigUtils.DEFAULT_SINGLE_FORMAT_INSTRUCTION);
        TranslatableLang recipeListTitle = recipeList
            .getTitleLangBuilder()
            .translation("zh_cn", "配方列表")
            .translation("en_us", "Recipe List")
            .build();
        PlaceholderLang recipeListTitlePlaceholder = recipeListTitle.getPlaceholder(ChatFormatting.WHITE);
        this.recipeList = recipeList.setTitle(recipeListTitle).build();

        ModuleOptionBuilder<ListMode> recipeListMode = ConfigUtils.createListModeOptionBuilderWithTitle(
            builder, this.recipeList, ListMode.BLACKLIST
        );
        this.recipeListMode = recipeListMode
            .setTooltip(recipeListMode
                .getTooltipLangBuilder()
                .translation(
                    "zh_cn",
                    """
                        %s
                        %s模式 - 如果切石配方的ID存在于【%s】/ 符合【%s】中的匹配条件，
                                 则不会根据该配方生成配方
                        %s模式 - 如果切石配方的ID不存在于【%s】/ 不符合【%s】中的匹配条件，
                                 则不会根据该配方生成配方"""
                ).translation(
                    "en_us",
                    """
                        %s
                        %s Mode: If a recipe's id exists in [%s] / matches any condition in [%s],
                                 new recipes based on it will not be generated
                        %s Mode: If a recipe's id does not exist in [%s] / match any condition in [%s],
                                 new recipes based on it will not be generated"""
                ).child(ConfigUtils.DEFAULT_LIST_MODE_TOOLTIP_HEAD)
                .child(ConfigUtils.DEFAULT_LIST_MODE_BLACKLIST)
                .child(recipeListTitlePlaceholder)
                .child(recipeListTitlePlaceholder)
                .child(ConfigUtils.DEFAULT_LIST_MODE_WHITELIST)
                .child(recipeListTitlePlaceholder)
                .child(recipeListTitlePlaceholder)
                .format(ChatFormatting.GRAY)
                .build()
            ).build();

        return builder
            .setTitle(builder
                .getTitleLangBuilder()
                .translation("zh_cn", "可逆切石")
                .translation("en_us", "Reversible Cutting")
                .build()
            ).defineEnabled(enabled
                .setTooltip(enabled
                    .getTooltipLangBuilder()
                    .translation(
                        "zh_cn",
                        "部分切石配方的材料和产物可以任意相互转换"
                    ).translation(
                        "en_us",
                        "The ingredients and products in some stonecutting recipes can be mutually " +
                            "converted into each other arbitrarily"
                    ).build()
                ).build()
            ).defineOption(this.recipeList)
            .defineOption(this.recipeListMode)
            .build();
    }
}

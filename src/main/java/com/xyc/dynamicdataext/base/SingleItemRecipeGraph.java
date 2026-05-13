package com.xyc.dynamicdataext.base;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.xyc.dynamicdataext.utils.CriterionUtils;
import com.xyc.dynamicdataext.utils.RecipeUtils;
import com.xyc.dynamicdataext.utils.RegistryUtils;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.apache.commons.lang3.ArrayUtils;

import java.util.*;

public class SingleItemRecipeGraph {
    private final Map<Item, Item> roots = new LinkedHashMap<>();                // 目标物品对应的根物品
    private final Map<Item, Integer> counts = new LinkedHashMap<>();            // 一个根物品可切成多少个目标物品
    private final Multimap<Item, Item> parents = LinkedHashMultimap.create();   // 目标物品对应的直接原材料

    public void addConnection(Item parent, ItemStack item) {
        this.addConnection(parent, item.getItem(), item.getCount());
    }

    public void addConnection(Item parent, Item item) {
        this.addConnection(parent, item, 1);
    }

    public void addConnection(Item parent, Item result, int resultCount) {
        if (this.roots.containsValue(result)) {
            Item root = this.findRoot(parent);
            for (Map.Entry<Item, Item> entry : this.roots.entrySet())
                if (entry.getValue().equals(result)) {
                    Item key = entry.getKey();
                    this.roots.put(key, root);
                    this.counts.put(key, resultCount * this.counts.get(key));
                }
        } else {
            this.roots.put(result, this.findRoot(parent));
            this.counts.put(result, resultCount * this.counts.get(parent));
        }
        this.parents.put(result, parent);
    }

    public Item findRoot(Item item) {
        if (this.roots.containsKey(item)) {
            Item parent = this.roots.get(item);
            if (parent != null)
                return parent.equals(item) ? item : this.findRoot(parent);
        }
        this.roots.put(item, item);
        this.counts.put(item, 1);
        return item;
    }

    public Map<Item, Map<Item, Integer>> getAllGroups() {
        Map<Item, Map<Item, Integer>> output = new LinkedHashMap<>();
        for (Map.Entry<Item, Item> entry : this.roots.entrySet()) {
            Item item = entry.getKey();
            output.computeIfAbsent(entry.getValue(), r -> new LinkedHashMap<>())
                  .put(item, this.counts.get(item));
        }
        return output;
    }

    public void clear() {
        this.roots.clear();
        this.counts.clear();
        this.parents.clear();
    }

    public Set<DynamicRecipeEntry> createAllRecipes(Module module, boolean reversible) {
        return createAllRecipes(module, reversible, "_cutting");
    }

    public Set<DynamicRecipeEntry> createAllRecipes(Module module, boolean reversible, String... recipeIdSuffix) {
        String recipeIdSuffixStr = "_" + String.join("_", recipeIdSuffix);
        Set<DynamicRecipeEntry> output = new LinkedHashSet<>();
        for (Map.Entry<Item, Map<Item, Integer>> group : this.getAllGroups().entrySet()) {
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

                // 获取比当前物品count更小的count值
                Integer[] upperLayerCounts = ArrayUtils.subarray(counts, 0, resultLayerIdx);

                for (Item result : resultLayerItems) {
                    // 同层物品合成
                    Set<Item> ingredientSet = new LinkedHashSet<>();
                    for (Item ingredientItem : resultLayerItems)
                        if (!ingredientItem.equals(result)) {
                            if (reversible || this.parents.containsEntry(result, ingredientItem))
                                ingredientSet.add(ingredientItem);
                        }


                    ResourceLocation resultLocation = RegistryUtils.getItemLocation(result);
                    if (resultLocation == null)
                        continue;
                    String resultNamespace = resultLocation.getNamespace();
                    String resultId = resultNamespace.equals("minecraft")
                        ? resultNamespace + "__" + resultLocation.getPath() // 防止不同命名空间同名物体造成配方ID重复
                        : resultLocation.getPath();
                    String recipeId = resultId + recipeIdSuffixStr;

                    if (!ingredientSet.isEmpty()) {
                        Item[] ingredients = ingredientSet.toArray(new Item[0]);
                        output.add(RecipeUtils.createRecipeEntry(
                            module,
                            recipeId,
                            SingleItemRecipeBuilder.stonecutting(
                                Ingredient.of(ingredients),
                                RecipeCategory.BUILDING_BLOCKS,
                                result,
                                1
                            ).unlockedBy("has_materials", CriterionUtils.hasItems(ingredients))
                        ));
                    }

                    // count更小的层合成
                    for (int upperLayerCount : upperLayerCounts) {
                        Item[] upperLayerIngredients = countToItemArray.get(upperLayerCount);
                        if (resultLayerCount % upperLayerCount > 0)
                            continue;
                        int resultCount = resultLayerCount / upperLayerCount;
                        String upperLayerRecipeId = recipeId + "_" + resultCount + recipeIdSuffixStr;
                        output.add(RecipeUtils.createRecipeEntry(
                            module,
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
    }
}

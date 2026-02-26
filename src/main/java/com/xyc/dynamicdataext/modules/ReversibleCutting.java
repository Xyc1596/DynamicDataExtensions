package com.xyc.dynamicdataext.modules;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.xyc.dynamicdataext.DynamicDataMain;
import com.xyc.dynamicdataext.DynamicDataRegistry;
import com.xyc.dynamicdataext.base.DynamicRecipeEntry;
import com.xyc.dynamicdataext.base.IRecipeManagerExtensions;
import com.xyc.dynamicdataext.base.Module;
import com.xyc.dynamicdataext.config.ModuleConfig;
import com.xyc.dynamicdataext.config.ModuleConfigBuilder;
import com.xyc.dynamicdataext.config.ModuleOptionBuilder;
import com.xyc.dynamicdataext.utils.ConfigUtils;
import com.xyc.dynamicdataext.utils.CriterionUtils;
import com.xyc.dynamicdataext.utils.LocationUtils;
import com.xyc.dynamicdataext.utils.RecipeUtils;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.StonecutterRecipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ReversibleCutting extends Module {
    private final Multimap<Item, Item> map = LinkedHashMultimap.create(), reversedMap = LinkedHashMultimap.create();

    public ReversibleCutting() {
        super(DynamicDataMain.MOD_ID, "reversible_cutting");
    }

    @Override
    public @Nullable Predicate<Recipe<?>> gatherRecipeExcluder() {
        IRecipeManagerExtensions manager = DynamicDataRegistry.getRecipeManager();
        return recipe -> {
            if (recipe instanceof StonecutterRecipe r) {
                ItemStack resultStack = manager.getResultItemStack(r);
                if (resultStack.getCount() == 1) {
                    Item resultItem = resultStack.getItem();
                    for (Ingredient ingredient : r.getIngredients())
                        for (ItemStack itemStack : ingredient.getItems()) {
                            Item ingredientItem = itemStack.getItem();
                            this.map.put(ingredientItem, resultItem);
                            this.reversedMap.put(resultItem, ingredientItem);
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
            Map<String, DynamicRecipeEntry> output = new LinkedHashMap<>();
            Set<Map.Entry<Item, Collection<Item>>> entries = Sets.union(
                map.asMap().entrySet(), reversedMap.asMap().entrySet()
            );

            for (Map.Entry<Item, Collection<Item>> entry : entries) {
                Item result = entry.getKey();
                String recipeId = LocationUtils.getItemId(result);
                if (output.containsKey(recipeId))
                    continue;
                Item[] ingredients = entry.getValue().toArray(new Item[0]);
                output.put(
                    recipeId,
                    RecipeUtils.createRecipeEntry(
                        this,
                        recipeId,
                        SingleItemRecipeBuilder.stonecutting(
                            Ingredient.of(ingredients),
                            RecipeCategory.BUILDING_BLOCKS,
                            result
                        ).unlockedBy("has_material", CriterionUtils.hasItems(ingredients))
                    ));
            }
            return new LinkedHashSet<>(output.values());
        };
    }

    @Override
    protected @NotNull ModuleConfig buildConfig() {
        ModuleConfigBuilder builder = this.createConfigBuilder();
        ModuleOptionBuilder<Boolean> enabled = ConfigUtils.createEnabledOptionBuilderWithTitle(builder);
        return super.buildConfig();
    }
}

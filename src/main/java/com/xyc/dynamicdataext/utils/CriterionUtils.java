package com.xyc.dynamicdataext.utils;

import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;

import javax.annotation.ParametersAreNonnullByDefault;

@SuppressWarnings("unused")
public final class CriterionUtils {
    @ParametersAreNonnullByDefault
    public static Criterion<RecipeUnlockedTrigger.TriggerInstance> recipeUnlocked(String recipeId) {
        return RecipeUnlockedTrigger.unlocked(LocationUtils.withDefaultNamespace(recipeId));
    }

    @ParametersAreNonnullByDefault
    public static Criterion<RecipeUnlockedTrigger.TriggerInstance> recipeUnlocked(String namespace, String recipeId) {
        return RecipeUnlockedTrigger.unlocked(LocationUtils.fromNamespaceAndPath(namespace, recipeId));
    }

    @ParametersAreNonnullByDefault
    public static Criterion<RecipeUnlockedTrigger.TriggerInstance> recipeUnlocked(ResourceLocation recipeLocation) {
        return RecipeUnlockedTrigger.unlocked(recipeLocation);
    }

    @ParametersAreNonnullByDefault
    public static Criterion<InventoryChangeTrigger.TriggerInstance> hasSingleIngredient(Ingredient ingredient) {
        return InventoryChangeTrigger.TriggerInstance.hasItems(ingredient.getItems()[0].getItem());
    }

    @ParametersAreNonnullByDefault
    public static Criterion<InventoryChangeTrigger.TriggerInstance> hasItems(Item... items) {
        return InventoryChangeTrigger.TriggerInstance.hasItems(items);
    }

    @ParametersAreNonnullByDefault
    public static Criterion<InventoryChangeTrigger.TriggerInstance> hasTag(TagKey<Item> tag) {
        return InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(tag).build());
    }
}

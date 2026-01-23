package com.xyc.practicalextensions.modules.contents;

import com.xyc.practicalextensions.modules.IModule;
import com.xyc.practicalextensions.utils.Cooking;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.Set;

public class LeatherFromRottenFlesh implements IModule {
    @Override
    public String getId() {
        return "leather_from_rotten_flesh";
    }

    @Override
    public Set<RecipeHolder<Recipe<?>>> gatherRecipesToAdd() {
        return Cooking.createSmokingAll(
            getId(),
            Ingredient.of(Items.ROTTEN_FLESH),
            RecipeCategory.MISC,
            Items.LEATHER,
            .1f,
            200
        );
    }
}

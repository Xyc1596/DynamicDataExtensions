package com.xyc.practicalextensions.modules.contents;

import com.xyc.practicalextensions.ModMain;
import com.xyc.practicalextensions.Utils;
import com.xyc.practicalextensions.modules.Module;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.Map;
import java.util.Set;

public class LeatherFromRottenFlesh extends Module {
    public LeatherFromRottenFlesh() {
        super(ModMain.MOD_ID, "leather_from_rotten_flesh");
    }

    @Override
    public Set<RecipeHolder<Recipe<?>>> gatherRecipesToAdd() {
        return Utils.createSmokingAll(
            ID,
            Ingredient.of(Items.ROTTEN_FLESH),
            RecipeCategory.MISC,
            Items.LEATHER,
            .1f,
            200
        );
    }

    @Override
    public Map<String, String> getOptionTranslations() {
        return Map.of(
            "zh_cn", "腐肉换皮革",
            "en_us", "Leather from Rotten Flesh"
        );
    }

    @Override
    public Map<String, String> getTooltipTranslations() {
        return Map.of(
            "zh_cn", "让腐肉变得更有用",
            "en_us", "Make rotten flesh more useful."
        );
    }
}

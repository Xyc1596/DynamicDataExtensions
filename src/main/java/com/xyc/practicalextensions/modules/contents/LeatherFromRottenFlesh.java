package com.xyc.practicalextensions.modules.contents;

import com.xyc.practicalextensions.ModMain;
import com.xyc.practicalextensions.base.Module;
import com.xyc.practicalextensions.base.Utils;
import com.xyc.practicalextensions.lang.ModuleLang;
import com.xyc.practicalextensions.lang.ModuleLangBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

public class LeatherFromRottenFlesh extends Module {
    public LeatherFromRottenFlesh() {
        super(ModMain.MOD_ID, "leather_from_rotten_flesh");
    }

    @Override
    public @NotNull Set<RecipeHolder<Recipe<?>>> gatherRecipesToAdd() {
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
    protected ModuleLang.@NotNull TranslatableLang buildOptionLang() {
        return ModuleLangBuilder.translatable("option", ModMain.MOD_ID, ID)
                                .translation("zh_cn", "腐肉换皮革")
                                .translation("en_us", "Leather from Rotten Flesh")
                                .build();
    }

    @Override
    protected @NotNull ModuleLang.TranslatableLang buildTooltipLang() {
        return ModuleLangBuilder.translatable("tooltip", ModMain.MOD_ID, ID)
                                .translation("zh_cn", "让腐肉变得更有用")
                                .translation("en_us", "Make rotten flesh more useful.")
                                .build();
    }
}

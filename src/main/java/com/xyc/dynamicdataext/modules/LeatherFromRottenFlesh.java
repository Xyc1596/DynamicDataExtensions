package com.xyc.dynamicdataext.modules;

import com.xyc.dynamicdataext.ModMain;
import com.xyc.dynamicdataext.base.Module;
import com.xyc.dynamicdataext.base.ModuleUtils;
import com.xyc.dynamicdataext.lang.ModuleLangBuilder;
import com.xyc.dynamicdataext.lang.TranslatableLang;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class LeatherFromRottenFlesh extends Module {
    public LeatherFromRottenFlesh() {
        super(ModMain.MOD_ID, "leather_from_rotten_flesh");
    }

    @Override
    public @NotNull Set<RecipeHolder<Recipe<?>>> gatherRecipesToAdd() {
        return ModuleUtils.createSmokingAll(
            this.namespace,
            this.id,
            Ingredient.of(Items.ROTTEN_FLESH),
            RecipeCategory.MISC,
            Items.LEATHER,
            .1f,
            200
        );
    }

    @Override
    protected @NotNull TranslatableLang buildOptionLang() {
        return ModuleLangBuilder.translatable("option", this.namespace, this.id)
                                .translation("zh_cn", "腐肉换皮革")
                                .translation("en_us", "Leather from Rotten Flesh")
                                .build();
    }

    @Override
    protected @NotNull TranslatableLang buildTooltipLang() {
        return ModuleLangBuilder.translatable("tooltip", this.namespace, this.id)
                                .translation("zh_cn", "让腐肉变得更有用")
                                .translation("en_us", "Make rotten flesh more useful")
                                .build();
    }
}

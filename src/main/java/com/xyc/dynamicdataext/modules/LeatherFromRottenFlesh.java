package com.xyc.dynamicdataext.modules;

import com.xyc.dynamicdataext.ModMain;
import com.xyc.dynamicdataext.base.Module;
import com.xyc.dynamicdataext.base.RecipeEntry;
import com.xyc.dynamicdataext.lang.TranslatableLang;
import com.xyc.dynamicdataext.utils.RecipeUtils;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class LeatherFromRottenFlesh extends Module {
    public LeatherFromRottenFlesh() {
        super(ModMain.MOD_ID, "leather_from_rotten_flesh");
    }

    @Override
    public @NotNull Set<RecipeEntry> gatherRecipesToAdd() {
        return RecipeUtils.createSmokingAll(
            this,
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
        return this.getOptionLangBuilder()
                   .translation("zh_cn", "腐肉换皮革")
                   .translation("en_us", "Leather from Rotten Flesh")
                   .build();
    }

    @Override
    protected @NotNull TranslatableLang buildTooltipLang() {
        return this.getTooltipLangBuilder()
                   .translation("zh_cn", "腐肉烧炼 / 烟熏 / 营火烹饪成皮革")
                   .translation("en_us", "Smelt / smoke / campfire cook rotten flesh into leather")
                   .build();
    }
}

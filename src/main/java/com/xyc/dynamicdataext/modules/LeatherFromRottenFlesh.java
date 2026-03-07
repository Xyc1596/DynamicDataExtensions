package com.xyc.dynamicdataext.modules;

import com.xyc.dynamicdataext.DynamicDataMain;
import com.xyc.dynamicdataext.base.DynamicRecipeEntry;
import com.xyc.dynamicdataext.base.Module;
import com.xyc.dynamicdataext.config.ModuleConfig;
import com.xyc.dynamicdataext.config.ModuleOptionBuilder;
import com.xyc.dynamicdataext.lang.TranslatableLang;
import com.xyc.dynamicdataext.utils.ConfigUtils;
import com.xyc.dynamicdataext.utils.RecipeUtils;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class LeatherFromRottenFlesh extends Module {
    public static final LeatherFromRottenFlesh INSTANCE = new LeatherFromRottenFlesh(
        DynamicDataMain.MOD_ID, "leather_from_rotten_flesh"
    );
    public static final TranslatableLang TITLE = INSTANCE.configBuilder
        .getTitleLangBuilder()
        .translation("zh_cn", "腐肉换皮革")
        .translation("en_us", "Leather from Rotten Flesh")
        .build();

    protected LeatherFromRottenFlesh(String namespace, String moduleId) {
        super(namespace, moduleId);
    }

    @Override
    public @NotNull Set<DynamicRecipeEntry> gatherRecipesToAdd() {
        return RecipeUtils.createSmokingAll(
            this,
            this.moduleId,
            Ingredient.of(Items.ROTTEN_FLESH),
            RecipeCategory.MISC,
            Items.LEATHER,
            .1f,
            200
        );
    }

    @Override
    protected @NotNull ModuleConfig buildConfig() {
        ModuleOptionBuilder<Boolean> enabled = ConfigUtils.createEnabledOptionBuilderWithTitle(this.configBuilder);
        return this.configBuilder
            .setTitle(TITLE)
            .defineEnabled(enabled
                .setDefaultValue(true)
                .setTooltip(enabled
                    .getTooltipLangBuilder()
                    .translation("zh_cn", "腐肉烧炼 / 烟熏 / 营火烹饪成皮革")
                    .translation("en_us", "Smelt / smoke / campfire cook rotten flesh into leather")
                    .build()
                ).build()
            ).build();
    }
}

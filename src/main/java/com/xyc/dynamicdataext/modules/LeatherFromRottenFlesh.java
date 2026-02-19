package com.xyc.dynamicdataext.modules;

import com.xyc.dynamicdataext.DynamicDataMain;
import com.xyc.dynamicdataext.base.Module;
import com.xyc.dynamicdataext.base.RecipeEntry;
import com.xyc.dynamicdataext.config.ModuleConfig;
import com.xyc.dynamicdataext.config.ModuleConfigBuilder;
import com.xyc.dynamicdataext.config.ModuleOptionBuilder;
import com.xyc.dynamicdataext.utils.ConfigUtils;
import com.xyc.dynamicdataext.utils.RecipeUtils;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class LeatherFromRottenFlesh extends Module {
    public LeatherFromRottenFlesh() {
        super(DynamicDataMain.MOD_ID, "leather_from_rotten_flesh");
    }

    @Override
    public @NotNull Set<RecipeEntry> gatherRecipesToAdd() {
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
        ModuleConfigBuilder builder = this.createConfigBuilder();
        ModuleOptionBuilder<Boolean> enabled = ConfigUtils.createEnabledOptionBuilder(builder);
        return builder.setTitle(builder
            .getTitleLangBuilder()
            .translation("zh_cn", "腐肉换皮革")
            .translation("en_us", "Leather from Rotten Flesh")
            .build()
        ).defineEnabled(enabled
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

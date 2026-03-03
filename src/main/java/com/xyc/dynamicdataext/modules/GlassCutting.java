package com.xyc.dynamicdataext.modules;

import com.xyc.dynamicdataext.DynamicDataMain;
import com.xyc.dynamicdataext.base.DynamicRecipeEntry;
import com.xyc.dynamicdataext.base.Module;
import com.xyc.dynamicdataext.config.ModuleConfig;
import com.xyc.dynamicdataext.config.ModuleConfigBuilder;
import com.xyc.dynamicdataext.config.ModuleOption;
import com.xyc.dynamicdataext.config.ModuleOptionBuilder;
import com.xyc.dynamicdataext.utils.*;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

public class GlassCutting extends Module {
    protected ModuleOption<Boolean> paneToGlass;

    public GlassCutting() {
        super(DynamicDataMain.MOD_ID, "glass_cutting");
    }

    @Override
    public @NotNull Set<DynamicRecipeEntry> gatherRecipesToAdd() {
        Set<DynamicRecipeEntry> output = new LinkedHashSet<>();

        Optional<HolderSet.Named<Item>> glassBlocks_ = RegistryUtils.getItemTagContents(Tags.Items.GLASS_BLOCKS);
        if (glassBlocks_.isEmpty())
            return output;

        for (Holder<Item> holder : glassBlocks_.get()) {
            Item glassBlock = holder.value();
            String glassBlockName = RegistryUtils.getItemId(glassBlock);
            String glassPaneName = glassBlockName + "_pane";
            Optional<Item> glassPane_ = RegistryUtils.getItem(LocationUtils.withDefaultNamespace(glassPaneName));
            if (glassPane_.isEmpty())
                continue;
            Item glassPane = glassPane_.get();
            output.add(RecipeUtils.createRecipeEntry(
                this,
                glassPaneName + "_from_cutting",
                SingleItemRecipeBuilder.stonecutting(
                    Ingredient.of(glassBlock),
                    RecipeCategory.MISC,
                    glassPane,
                    4
                ).unlockedBy("has_material", CriterionUtils.hasItems(glassBlock))
            ));
            if (paneToGlass.getValue()) {
                output.add(RecipeUtils.createRecipeEntry(
                    this,
                    glassBlockName + "_from_pane",
                    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, glassBlock)
                                       .define('#', glassPane).pattern("##").pattern("##")
                                       .unlockedBy("has_material", CriterionUtils.hasItems(glassPane))
                ));
            }
        }
        return output;
    }

    @Override
    protected @NotNull ModuleConfig buildConfig() {
        ModuleConfigBuilder builder = this.createConfigBuilder();
        ModuleOptionBuilder<Boolean> enabled = ConfigUtils.createEnabledOptionBuilderWithTitle(builder);
        ModuleOptionBuilder<Boolean> paneToGlass = builder.createBooleanOptionBuilder("pane_to_glass");
        this.paneToGlass = paneToGlass.setTitle(paneToGlass
            .getTitleLangBuilder()
            .translation("zh_cn", "玻璃板合成玻璃")
            .translation("en_us", "Glass Pane to Glass Block")
            .build()
        ).setTooltip(paneToGlass
            .getTooltipLangBuilder()
            .translation("zh_cn", "4玻璃板 → 1玻璃（合成）")
            .translation("en_us", "4 glass panes → 1 glass block (crafting)")
            .build()
        ).setDefaultValue(true).build();

        return builder.setTitle(builder
            .getTitleLangBuilder()
            .translation("zh_cn", "切玻璃")
            .translation("en_us", "Glass Cutting")
            .build()
        ).defineEnabled(enabled
            .setTooltip(enabled
                .getTooltipLangBuilder()
                .translation("zh_cn", "1玻璃 → 4玻璃板（切石）")
                .translation("en_us", "1 glass block → 4 glass panes (stonecutting)")
                .build()
            ).build()
        ).defineOption(this.paneToGlass).build();
    }
}

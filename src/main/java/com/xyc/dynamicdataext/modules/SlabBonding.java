package com.xyc.dynamicdataext.modules;

import com.xyc.dynamicdataext.DynamicDataMain;
import com.xyc.dynamicdataext.base.DynamicRecipeEntry;
import com.xyc.dynamicdataext.base.Module;
import com.xyc.dynamicdataext.config.ModuleConfig;
import com.xyc.dynamicdataext.config.ModuleOptionBuilder;
import com.xyc.dynamicdataext.lang.TranslatableLang;
import com.xyc.dynamicdataext.utils.*;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

public class SlabBonding extends Module {
    public static final SlabBonding INSTANCE = new SlabBonding(DynamicDataMain.MOD_ID, "slab_bonding");
    public static final TranslatableLang TITLE = INSTANCE.configBuilder
        .getTitleLangBuilder()
        .translation("zh_cn", "台阶拼合")
        .translation("en_us", "Slab Bonding")
        .build();

    protected SlabBonding(String namespace, String moduleId) {
        super(namespace, moduleId);
    }

    @Override
    public @NotNull Set<DynamicRecipeEntry> gatherRecipesToAdd() {
        final String BRICK_SUFFIX = "_brick_slab", NORMAL_SUFFIX = "_slab";
        Set<DynamicRecipeEntry> output = new LinkedHashSet<>();
        RegistryUtils
            .getItemTagContents(ItemTags.SLABS)
            .ifPresent(holders -> holders.forEach(
                holder -> RegistryUtils.getLocation(holder).ifPresent(
                    location -> {
                        String name = location.getPath();
                        String resultName = holder.is(ItemTags.WOODEN_SLABS)
                            ? StringUtils.stripEnd(name, NORMAL_SUFFIX) + "_planks"
                            : name.endsWith(BRICK_SUFFIX)
                            ? StringUtils.stripEnd(name, BRICK_SUFFIX) + "s"
                            : StringUtils.stripEnd(name, "_slab");
                        Optional<Item> result_ = RegistryUtils.getItem(
                            LocationUtils.fromNamespaceAndPath(location.getNamespace(), resultName)
                        );
                        if (result_.isPresent()) {
                            Item result = result_.get();
                            output.add(
                                RecipeUtils.createRecipeEntry(
                                    this,
                                    resultName + "_from_" + name,
                                    ShapelessRecipeBuilder
                                        .shapeless(RecipeCategory.BUILDING_BLOCKS, result)
                                        .requires(holder.value(), 2)
                                        .unlockedBy("has_material", CriterionUtils.hasItems(result))
                                )
                            );
                        }
                    }
                )
            ));
        return output;
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
                    .translation("zh_cn", "2 同种台阶 → 原方块")
                    .translation("en_us", "2 slabs of same material → original block")
                    .build()
                ).build()
            ).build();
    }
}

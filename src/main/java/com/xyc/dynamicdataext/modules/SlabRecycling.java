package com.xyc.dynamicdataext.modules;

import com.xyc.dynamicdataext.DynamicDataMain;
import com.xyc.dynamicdataext.base.Module;
import com.xyc.dynamicdataext.base.DynamicRecipeEntry;
import com.xyc.dynamicdataext.config.ModuleConfig;
import com.xyc.dynamicdataext.config.ModuleConfigBuilder;
import com.xyc.dynamicdataext.config.ModuleOptionBuilder;
import com.xyc.dynamicdataext.utils.ConfigUtils;
import com.xyc.dynamicdataext.utils.CriterionUtils;
import com.xyc.dynamicdataext.utils.LocationUtils;
import com.xyc.dynamicdataext.utils.RecipeUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashSet;
import java.util.Set;

public class SlabRecycling extends Module {
    public SlabRecycling() {
        super(DynamicDataMain.MOD_ID, "slab_recycling");
    }

    @Override
    public @NotNull Set<DynamicRecipeEntry> gatherRecipesToAdd() {
        final String BRICK_SUFFIX = "_brick_slab", NORMAL_SUFFIX = "_slab";
        Set<DynamicRecipeEntry> output = new LinkedHashSet<>();
        BuiltInRegistries.ITEM
            .getTag(ItemTags.SLABS)
            .ifPresent(holders -> holders.forEach(
                holder -> holder.unwrapKey().ifPresent(
                    key -> {
                        ResourceLocation location = key.location();
                        String name = location.getPath();
                        String resultName = holder.is(ItemTags.WOODEN_SLABS)
                            ? StringUtils.stripEnd(name, NORMAL_SUFFIX) + "_planks"
                            : name.endsWith(BRICK_SUFFIX)
                            ? StringUtils.stripEnd(name, BRICK_SUFFIX) + "s"
                            : StringUtils.stripEnd(name, "_slab");
                        Item result = BuiltInRegistries.ITEM.get(
                            LocationUtils.fromNamespaceAndPath(location.getNamespace(), resultName)
                        );
                        if (result != Items.AIR) {
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
        ModuleConfigBuilder builder = this.createConfigBuilder();
        ModuleOptionBuilder<Boolean> enabled = ConfigUtils.createEnabledOptionBuilderWithTitle(builder);
        return builder
            .setTitle(builder
                .getTitleLangBuilder()
                .translation("zh_cn", "台阶还原")
                .translation("en_us", "Slab Recycling")
                .build()
            ).defineEnabled(enabled
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

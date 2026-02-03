package com.xyc.dynamicdataext.modules;

import com.xyc.dynamicdataext.ModMain;
import com.xyc.dynamicdataext.base.Module;
import com.xyc.dynamicdataext.lang.ModuleLangBuilder;
import com.xyc.dynamicdataext.lang.TranslatableLang;
import com.xyc.dynamicdataext.utils.CriterionUtils;
import com.xyc.dynamicdataext.utils.RecipeUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class SlabRecycling extends Module {
    public SlabRecycling() {
        super(ModMain.MOD_ID, "slab_recycling");
    }

    @Override
    public @NotNull Set<RecipeHolder<Recipe<?>>> gatherRecipesToAdd() {
        final String BRICK_SUFFIX = "_brick_slab", NORMAL_SUFFIX = "_slab";
        final TagKey<Item> WOODEN_SLABS = RecipeUtils.createItemTagKey(
            ResourceLocation.withDefaultNamespace("wooden_slabs")
        );
        Set<RecipeHolder<Recipe<?>>> output = new HashSet<>();
        BuiltInRegistries.ITEM
            .getTag(RecipeUtils.createItemTagKey(ResourceLocation.withDefaultNamespace("slabs")))
            .ifPresent(holders -> holders.forEach(
                holder -> holder.unwrapKey().ifPresent(
                    key -> {
                        ResourceLocation location = key.location();
                        String name = location.getPath();
                        String resultName = holder.is(WOODEN_SLABS)
                            ? StringUtils.stripEnd(name, NORMAL_SUFFIX) + "_planks"
                            : name.endsWith(BRICK_SUFFIX)
                            ? StringUtils.stripEnd(name, BRICK_SUFFIX) + "s"
                            : StringUtils.stripEnd(name, "_slab");
                        Item result = BuiltInRegistries.ITEM.get(
                            ResourceLocation.fromNamespaceAndPath(location.getNamespace(), resultName)
                        );
                        if (result != Items.AIR) {
                            output.add(
                                RecipeUtils.createRecipeHolder(
                                    this.namespace,
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
    protected @NotNull TranslatableLang buildOptionLang() {
        return ModuleLangBuilder.translatable("option", this.namespace, this.id)
                                .translation("zh_cn", "台阶还原")
                                .translation("en_us", "Slab Recycling")
                                .build();
    }

    @Override
    protected @NotNull TranslatableLang buildTooltipLang() {
        return ModuleLangBuilder.translatable("tooltip", this.namespace, this.id)
                                .translation("zh_cn", "2 同种台阶 -> 原方块")
                                .translation("en_us", "2 slabs of same material -> original block")
                                .build();
    }
}

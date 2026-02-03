package com.xyc.dynamicdataext.modules;

import com.xyc.dynamicdataext.ModMain;
import com.xyc.dynamicdataext.base.Module;
import com.xyc.dynamicdataext.utils.RecipeUtils;
import com.xyc.dynamicdataext.lang.ModuleLangBuilder;
import com.xyc.dynamicdataext.lang.TranslatableLang;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RawOreBlockSmelting extends Module {
    public RawOreBlockSmelting() {
        super(ModMain.MOD_ID, "raw_ore_block_smelting");
    }

    @Override
    public @NotNull Set<RecipeHolder<Recipe<?>>> gatherRecipesToAdd() {
        Pattern materialPattern = Pattern.compile("storage_blocks/raw_(.*)");
        Set<RecipeHolder<Recipe<?>>> output = new HashSet<>();
        BuiltInRegistries.ITEM
            .getTag(RecipeUtils.createItemTagKey(RecipeUtils.withCommonNamespace("storage_blocks")))
            .ifPresent(holders -> holders.forEach(
                holder -> holder.tags().forEach(tagKey -> {
                    Matcher matcher = materialPattern.matcher(tagKey.location().getPath());
                    if (matcher.find()) {
                        String material = matcher.group(1);
                        BuiltInRegistries.ITEM.getTag(
                            RecipeUtils.createItemTagKey(
                                RecipeUtils.withCommonNamespace("storage_blocks/" + material)
                            )
                        ).ifPresent(
                            resultHolders -> output.addAll(
                                RecipeUtils.createBlastingAll(
                                    this.namespace,
                                    "raw_" + material + "_block",
                                    Ingredient.of(tagKey),
                                    RecipeCategory.MISC,
                                    resultHolders.get(0).value(),
                                    6.3f,
                                    1800
                                )
                            )
                        );
                    }
                })
            ));

        return output;
    }

    @Override
    protected @NotNull TranslatableLang buildOptionLang() {
        return ModuleLangBuilder.translatable("option", this.namespace, this.id)
                                .translation("zh_cn", "粗矿物块烧炼")
                                .translation("en_us", "Raw Ore Block Smelting")
                                .build();
    }

    @Override
    protected @NotNull TranslatableLang buildTooltipLang() {
        return ModuleLangBuilder.translatable("tooltip", this.namespace, this.id)
                                .translation("zh_cn", "粗矿物块可以直接烧炼成矿物块")
                                .translation("en_us", "Smelt raw mineral blocks directly into mineral blocks")
                                .build();
    }
}

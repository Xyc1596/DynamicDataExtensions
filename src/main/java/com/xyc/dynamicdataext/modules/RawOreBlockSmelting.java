package com.xyc.dynamicdataext.modules;

import com.xyc.dynamicdataext.ModMain;
import com.xyc.dynamicdataext.base.Module;
import com.xyc.dynamicdataext.base.RecipeEntry;
import com.xyc.dynamicdataext.lang.TranslatableLang;
import com.xyc.dynamicdataext.utils.RecipeUtils;
import com.xyc.dynamicdataext.utils.ResourceLocationUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RawOreBlockSmelting extends Module {
    public RawOreBlockSmelting() {
        super(ModMain.MOD_ID, "raw_ore_block_smelting");
    }

    @Override
    public @NotNull Set<RecipeEntry> gatherRecipesToAdd() {
        Pattern materialPattern = Pattern.compile("storage_blocks/raw_(.*)");
        Set<RecipeEntry> output = new LinkedHashSet<>();
        BuiltInRegistries.ITEM
            .getTag(ResourceLocationUtils.createItemTagKey(ResourceLocationUtils.withCommonNamespace("storage_blocks")))
            .ifPresent(holders -> holders.forEach(
                holder -> holder.tags().forEach(tagKey -> {
                    Matcher matcher = materialPattern.matcher(tagKey.location().getPath());
                    if (matcher.find()) {
                        String material = matcher.group(1);
                        BuiltInRegistries.ITEM.getTag(
                            ResourceLocationUtils.createItemTagKey(
                                ResourceLocationUtils.withCommonNamespace("storage_blocks/" + material)
                            )
                        ).ifPresent(
                            resultHolders -> output.addAll(
                                RecipeUtils.createBlastingAll(
                                    this,
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
        return this.getOptionLangBuilder()
                   .translation("zh_cn", "粗矿物块烧炼")
                   .translation("en_us", "Raw Ore Block Smelting")
                   .build();
    }

    @Override
    protected @NotNull TranslatableLang buildTooltipLang() {
        return this.getTooltipLangBuilder()
                   .translation("zh_cn", "粗矿物块可以直接烧炼成矿物块")
                   .translation("en_us", "Smelt raw mineral blocks directly into mineral blocks")
                   .build();
    }
}

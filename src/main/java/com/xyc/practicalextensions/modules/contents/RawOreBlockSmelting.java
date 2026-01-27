package com.xyc.practicalextensions.modules.contents;

import com.xyc.practicalextensions.ModMain;
import com.xyc.practicalextensions.base.Module;
import com.xyc.practicalextensions.base.Utils;
import com.xyc.practicalextensions.lang.ModuleLang;
import com.xyc.practicalextensions.lang.ModuleLangBuilder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RawOreBlockSmelting extends Module {
    public RawOreBlockSmelting() {
        super(ModMain.MOD_ID, "raw_ore_block_smelting");
    }

    @Override
    public @NotNull Set<RecipeHolder<Recipe<?>>> gatherRecipesToAdd() {
        Pattern materialPattern = Pattern.compile("storage_blocks/raw_(.*)");
        Map<String, HolderSet.Named<Block>> materials = new HashMap<>();

        BuiltInRegistries.BLOCK.getTags().forEach(
            p -> {
                ResourceLocation location = p.getFirst().location();
                Matcher matcher = materialPattern.matcher(location.getPath());
                boolean matched = matcher.find();
                if (matched) {
                    materials.put(matcher.group(1), p.getSecond());
                }
            }
        );

        Set<RecipeHolder<Recipe<?>>> output = new HashSet<>();
        materials.forEach(
            (material, holders) -> {
                String id = "raw_" + material + "_block";
                TagKey<Item> resultKey = TagKey.create(
                    Registries.ITEM,
                    ResourceLocation.fromNamespaceAndPath("c", "storage_blocks/" + material)
                );
                Optional<HolderSet.Named<Item>> resultHolders = BuiltInRegistries.ITEM.getTag(resultKey);
                if (resultHolders.isEmpty())
                    return;

                Item result = resultHolders.get().get(0).value();
                holders.forEach(
                    h -> output.addAll(
                        Utils.createBlastingAll(
                            id,
                            Ingredient.of(h.value()),
                            RecipeCategory.MISC,
                            result,
                            6.3f,
                            1800
                        )
                    )
                );
            }
        );
        return output;
    }

    @Override
    protected @NotNull ModuleLang.TranslatableLang buildOptionLang() {
        return ModuleLangBuilder.translatable("option", ModMain.MOD_ID, ID)
                                .translation("zh_cn", "粗矿物块烧炼")
                                .translation("en_us", "Raw Ore Block Smelting")
                                .build();
    }

    @Override
    protected @NotNull ModuleLang.TranslatableLang buildTooltipLang() {
        return ModuleLangBuilder.translatable("tooltip", ModMain.MOD_ID, ID)
                                .translation("zh_cn", "粗矿物块可以直接烧炼成矿物块")
                                .translation("en_us", "Smelt raw mineral blocks directly into mineral blocks.")
                                .build();
    }
}

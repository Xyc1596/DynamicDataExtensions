package com.xyc.practicalextensions.modules;

import com.xyc.practicalextensions.ModMain;
import com.xyc.practicalextensions.base.Module;
import com.xyc.practicalextensions.base.ModuleUtils;
import com.xyc.practicalextensions.lang.ModuleLangBuilder;
import com.xyc.practicalextensions.lang.TranslatableLang;
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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
        for (Map.Entry<String, HolderSet.Named<Block>> entry : materials.entrySet()) {
            String material = entry.getKey();
            String recipeId = "raw_" + material + "_block";
            TagKey<Item> resultKey = TagKey.create(
                Registries.ITEM,
                ResourceLocation.fromNamespaceAndPath("c", "storage_blocks/" + material)
            );
            BuiltInRegistries.ITEM.getTag(resultKey).ifPresent(
                resultHolders -> entry.getValue().forEach(
                    h -> output.addAll(
                        ModuleUtils.createBlastingAll(
                            this.namespace,
                            recipeId,
                            Ingredient.of(h.value()),
                            RecipeCategory.MISC,
                            resultHolders.get(0).value(),
                            6.3f,
                            1800
                        )
                    )
                )
            );
        }
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

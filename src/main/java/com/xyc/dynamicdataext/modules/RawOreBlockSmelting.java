package com.xyc.dynamicdataext.modules;

import com.xyc.dynamicdataext.DynamicDataMain;
import com.xyc.dynamicdataext.base.EntryAndTagCollection;
import com.xyc.dynamicdataext.base.Module;
import com.xyc.dynamicdataext.base.RecipeEntry;
import com.xyc.dynamicdataext.config.ModuleConfig;
import com.xyc.dynamicdataext.config.ModuleConfigBuilder;
import com.xyc.dynamicdataext.config.ModuleOption;
import com.xyc.dynamicdataext.config.ModuleOptionBuilder;
import com.xyc.dynamicdataext.lang.TranslatableLang;
import com.xyc.dynamicdataext.utils.ConfigUtils;
import com.xyc.dynamicdataext.utils.ConfigUtils.ListMode;
import com.xyc.dynamicdataext.utils.LocationUtils;
import com.xyc.dynamicdataext.utils.RecipeUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class RawOreBlockSmelting extends Module {
    protected ModuleOption<List<String>> ingredientList;
    protected ModuleOption<ListMode> ingredientListMode;

    public RawOreBlockSmelting() {
        super(DynamicDataMain.MOD_ID, "raw_ore_block_smelting");
    }

    @Override
    public @NotNull Set<RecipeEntry> gatherRecipesToAdd() {
        EntryAndTagCollection<Item> ingredientList = EntryAndTagCollection
            .items().parseStrings(this.ingredientList.getValue());
        Pattern materialPattern = Pattern.compile("storage_blocks/raw_(.*)");
        Set<RecipeEntry> output = new LinkedHashSet<>();
        boolean whitelistMode = this.ingredientListMode.getValue() == ListMode.WHITELIST;

        Optional<HolderSet.Named<Item>> storageBlockHolders = BuiltInRegistries.ITEM.getTag(Tags.Items.STORAGE_BLOCKS);
        if (storageBlockHolders.isEmpty())
            return Set.of();

        for (Holder<Item> storageBlockHolder : storageBlockHolders.get())
            storageBlockHolder.tags().forEach(ingredientTag -> {
                Matcher matcher = materialPattern.matcher(ingredientTag.location().getPath());
                if (!matcher.find())
                    return;

                String material = matcher.group(1);
                Optional<HolderSet.Named<Item>> resultHolders_ = BuiltInRegistries.ITEM.getTag(
                    LocationUtils.createItemTagKey(
                        LocationUtils.withCommonNamespace("storage_blocks/" + material)
                    )
                );
                if (resultHolders_.isEmpty())
                    return;

                HolderSet.Named<Item> resultHolder = resultHolders_.get();
                if (resultHolder.size() == 0)
                    return;

                Item result = resultHolder.get(0).value();
                Optional<HolderSet.Named<Item>> ingredientHolders_ = BuiltInRegistries.ITEM.getTag(ingredientTag);
                if (ingredientHolders_.isEmpty())
                    return;

                Set<Item> ingredientSet = ingredientHolders_.get().stream().map(Holder::value)
                                                            .collect(Collectors.toCollection(LinkedHashSet::new));
                Set<Item> filtered = ingredientList.applyToForSet(ingredientSet, whitelistMode);
                Ingredient ingredient = filtered.size() == ingredientSet.size()
                    ? Ingredient.of(ingredientTag)
                    : Ingredient.of(filtered.stream().map(ItemStack::new));
                if (ingredient.isEmpty())
                    return;

                output.addAll(
                    RecipeUtils.createBlastingAll(
                        this,
                        "raw_" + material + "_block",
                        ingredient,
                        RecipeCategory.MISC,
                        result,
                        6.3f,
                        1800
                    )
                );
            });

        return output;
    }

    @Override
    protected @NotNull ModuleConfig buildConfig() {
        ModuleConfigBuilder builder = this.createConfigBuilder();
        ModuleOptionBuilder<Boolean> enabled = ConfigUtils.createEnabledOptionBuilderWithTitle(builder);

        ModuleOptionBuilder<List<String>> ingredientListBuilder = ConfigUtils
            .createEntryListOptionBuilderWithTooltip(builder, "ingredient_list");
        TranslatableLang ingredientListTitle = ingredientListBuilder
            .getTitleLangBuilder()
            .translation("zh_cn", "原材料列表")
            .translation("en_us", "Ingredient List")
            .build();
        this.ingredientList = ingredientListBuilder.setTitle(ingredientListTitle).build();

        ModuleOptionBuilder<ListMode> ingredientListModeBuilder = ConfigUtils
            .createListModeOptionBuilderWithTitle(builder, this.ingredientList, ListMode.BLACKLIST);
        this.ingredientListMode = ingredientListModeBuilder
            .setTooltip(ingredientListModeBuilder
                .getTooltipLangBuilder()
                .translation(
                    "zh_cn",
                    """
                        %s
                        %s模式 - 如果配方的原材料%s【%s】中的物品，则该配方不会生成
                        %s模式 - 如果配方的原材料%s【%s】中的物品，则该配方不会生成"""
                ).translation(
                    "en_us",
                    """
                        %s
                        %s Mode: If a recipe's ingredients contain any item from [%s],
                                 the recipe will not be generated
                        %s Mode: If a recipe's ingredients do not contain any item from [%s],
                                 the recipe will not be generated"""
                ).child(ConfigUtils.DEFAULT_LIST_MODE_TOOLTIP_HEAD)
                .child(ConfigUtils.DEFAULT_LIST_MODE_BLACKLIST)
                .child(ingredientListModeBuilder
                    .createTooltipChildLangBuilder("contain")
                    .translation("zh_cn", "包含")
                    .translation("en_us", "contain any")
                    .format(ConfigUtils.DEFAULT_LIST_MODE_BLACKLIST.getFormats())
                    .build()
                ).child(ingredientListTitle.getPlaceholder())
                .child(ConfigUtils.DEFAULT_LIST_MODE_WHITELIST)
                .child(ingredientListModeBuilder
                    .createTooltipChildLangBuilder("not_contain")
                    .translation("zh_cn", "不含")
                    .translation("en_us", "do not contain any")
                    .format(ConfigUtils.DEFAULT_LIST_MODE_WHITELIST.getFormats())
                    .build()
                ).child(ingredientListTitle.getPlaceholder())
                .format(ChatFormatting.GRAY)
                .build()
            ).build();

        return builder
            .setTitle(builder
                .getTitleLangBuilder()
                .translation("zh_cn", "粗矿物块烧炼")
                .translation("en_us", "Raw Ore Block Smelting")
                .build()
            ).defineEnabled(enabled
                .setTooltip(enabled
                    .getTooltipLangBuilder()
                    .translation("zh_cn", "粗矿物块可以直接烧炼成矿物块")
                    .translation("en_us", "Smelt raw mineral blocks directly into mineral blocks")
                    .build()
                ).build()
            ).defineOption(this.ingredientList)
            .defineOption(this.ingredientListMode)
            .build();
    }
}

package com.xyc.dynamicdataext.utils;

import com.xyc.dynamicdataext.DynamicDataMain;
import com.xyc.dynamicdataext.config.ModuleConfigBuilder;
import com.xyc.dynamicdataext.config.ModuleOption;
import com.xyc.dynamicdataext.config.ModuleOptionBuilder;
import com.xyc.dynamicdataext.lang.ModuleLangBuilder;
import com.xyc.dynamicdataext.lang.TranslatableBuilder;
import com.xyc.dynamicdataext.lang.TranslatableLang;
import net.minecraft.ChatFormatting;
import org.apache.commons.lang3.ArrayUtils;

import java.util.List;

public final class ConfigUtils {
    public static final TranslatableLang DEFAULT_ENABLED_TITLE = createDefaultLangBuilder("enabled")
        .translation("zh_cn", "启用模块")
        .translation("en_us", "Enable")
        .build();

    public static final TranslatableLang DEFAULT_ITEM_LIST_INSTRUCTION =
        createDefaultLangBuilder("id_format_instruction")
            .translation(
                "zh_cn",
                """
                    命名空间ID格式：
                      * 单项 - %s
                      * 标签 - %s
                      > 命名空间为“minecraft”时，前缀“minecraft:”可省略"""
            ).translation(
                "en_us",
                """
                    Namespaced ID Formats:
                      * Single - %s
                      * Tag    - %s
                      > If the namespace is "minecraft", the prefix "minecraft:" can be omitted"""
            ).child(
                createDefaultLangBuilder("id_format_instruction", "single")
                    .translation("zh_cn", "<命名空间>:<路径>")
                    .translation("en_us", "<namespace>:<path>")
                    .format(ChatFormatting.WHITE)
                    .build()
            ).child(
                createDefaultLangBuilder("id_format_instruction", "tag")
                    .translation("zh_cn", "#<命名空间>:<路径>")
                    .translation("en_us", "#<namespace>:<path>")
                    .format(ChatFormatting.DARK_PURPLE)
                    .build()
            ).format(ChatFormatting.GRAY).build();

    public static final TranslatableLang DEFAULT_INGREDIENT_BLACKLIST_TITLE =
        createDefaultLangBuilder("ingredient_blacklist")
            .translation("zh_cn", "原材料黑名单")
            .translation("en_us", "Ingredient Blacklist")
            .build();

    public static final TranslatableLang DEFAULT_INGREDIENT_BLACKLIST_TOOLTIP =
        createDefaultLangBuilder("ingredient_blacklist", "tooltip")
            .translation("zh_cn", "如果配方的原材料包含列表中的物品，则该配方不会生成\n%s")
            .translation(
                "en_us",
                "If a recipe's ingredients include any item from this list, the recipe will not be generated\n%s"
            )
            .child(DEFAULT_ITEM_LIST_INSTRUCTION)
            .build();

    public static TranslatableBuilder createDefaultLangBuilder(String... id) {
        return ModuleLangBuilder.translatable(
            "module",
            DynamicDataMain.MOD_ID,
            ArrayUtils.insert(0, id, "default")
        );
    }

    public static ModuleOptionBuilder<Boolean> createEnabledOptionBuilder(ModuleConfigBuilder builder) {
        return createEnabledOptionBuilder(builder, true);
    }

    public static ModuleOptionBuilder<Boolean> createEnabledOptionBuilder(
        ModuleConfigBuilder builder,
        boolean defaultValue
    ) {
        return builder.createBooleanOptionBuilder("enabled")
                      .setDefaultValue(defaultValue)
                      .setTitle(DEFAULT_ENABLED_TITLE);
    }

    public static ModuleOption<List<String>> createIngredientBlacklistOption(ModuleConfigBuilder builder) {
        return createIngredientBlacklistOption(builder, List.of());
    }

    public static ModuleOption<List<String>> createIngredientBlacklistOption(
        ModuleConfigBuilder builder,
        List<String> defaultValue
    ) {
        return builder.createStringListOptionBuilder("ingredient_blacklist")
                      .setTitle(DEFAULT_INGREDIENT_BLACKLIST_TITLE)
                      .setTooltip(DEFAULT_INGREDIENT_BLACKLIST_TOOLTIP)
                      .setDefaultValue(defaultValue)
                      .build();
    }
}

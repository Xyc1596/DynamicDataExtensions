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

    public static final TranslatableLang DEFAULT_ID_FORMAT_INSTRUCTION_HEAD =
        createDefaultLangBuilder("id_format_instruction", "head")
            .translation("zh_cn", "命名空间ID格式")
            .translation("en_us", "Namespaced ID Formats")
            .format(ChatFormatting.ITALIC, ChatFormatting.WHITE)
            .build();
    public static final TranslatableLang DEFAULT_ID_FORMAT_INSTRUCTION_SINGLE =
        createDefaultLangBuilder("id_format_instruction", "single")
            .translation("zh_cn", "* 单项 - %s")
            .translation("en_us", "* Single - %s")
            .child(createDefaultLangBuilder("id_format_instruction", "single", "format")
                .translation("zh_cn", "<命名空间>:<路径>")
                .translation("en_us", "<namespace>:<path>")
                .format(ChatFormatting.WHITE)
                .build()
            ).format(ChatFormatting.GRAY)
            .build();
    public static final TranslatableLang DEFAULT_ID_FORMAT_INSTRUCTION_TAG =
        createDefaultLangBuilder("id_format_instruction", "tag")
            .translation("zh_cn", "* 标签 - %s")
            .translation("en_us", "* Tag    - %s")
            .child(createDefaultLangBuilder("id_format_instruction", "tag", "format")
                .translation("zh_cn", "#<命名空间>:<路径>")
                .translation("en_us", "#<namespace>:<path>")
                .format(ChatFormatting.DARK_PURPLE)
                .build()
            ).format(ChatFormatting.GRAY)
            .build();
    public static final TranslatableLang DEFAULT_ID_FORMAT_INSTRUCTION_COMMENT =
        createDefaultLangBuilder("id_format_instruction", "comment")
            .translation(
                "zh_cn",
                """
                    > 命名空间为“minecraft”时，前缀“minecraft:”可省略
                    > 添加“@”前缀以使用正则表达式匹配"""
            ).translation(
                "en_us",
                """
                    > If the namespace is "minecraft", the prefix "minecraft:" can be omitted
                    > Add "@" prefix to use regular expression matching"""
            ).format(ChatFormatting.GRAY, ChatFormatting.ITALIC)
            .build();
    public static final TranslatableLang DEFAULT_ID_FORMAT_INSTRUCTION =
        createDefaultLangBuilder("id_format_instruction")
            .translation("zh_cn", "%s\n%s\n%s\n%s")
            .translation("en_us", "%s\n%s\n%s\n%s")
            .child(DEFAULT_ID_FORMAT_INSTRUCTION_HEAD)
            .child(DEFAULT_ID_FORMAT_INSTRUCTION_SINGLE)
            .child(DEFAULT_ID_FORMAT_INSTRUCTION_TAG)
            .child(DEFAULT_ID_FORMAT_INSTRUCTION_COMMENT)
            .build();
    public static final TranslatableLang DEFAULT_SINGLE_FORMAT_INSTRUCTION =
        createDefaultLangBuilder("single_format_instruction")
            .translation("zh_cn", "%s\n%s\n%s")
            .translation("en_us", "%s\n%s\n%s")
            .child(DEFAULT_ID_FORMAT_INSTRUCTION_HEAD)
            .child(DEFAULT_ID_FORMAT_INSTRUCTION_SINGLE)
            .child(DEFAULT_ID_FORMAT_INSTRUCTION_COMMENT)
            .build();

    public static final TranslatableLang DEFAULT_LIST_MODE_BLACKLIST =
        createDefaultLangBuilder("list_mode", "blacklist")
            .translation("zh_cn", "黑名单")
            .translation("en_us", "Blacklist")
            .format(ChatFormatting.RED)
            .build();
    public static final TranslatableLang DEFAULT_LIST_MODE_WHITELIST =
        createDefaultLangBuilder("list_mode", "whitelist")
            .translation("zh_cn", "白名单")
            .translation("en_us", "Whitelist")
            .format(ChatFormatting.GREEN)
            .build();
    public static final TranslatableLang DEFAULT_LIST_MODE_TOOLTIP_HEAD =
        createDefaultLangBuilder("list_mode", "tooltip_head")
            .translation("zh_cn", "列表模式")
            .translation("en_us", "List Mode")
            .format(ChatFormatting.ITALIC, ChatFormatting.WHITE)
            .build();

    public static TranslatableBuilder createDefaultLangBuilder(String... id) {
        return ModuleLangBuilder.translatable(
            true,
            "module",
            DynamicDataMain.MOD_ID,
            ArrayUtils.insert(0, id, "_")
        );
    }

    public static ModuleOptionBuilder<Boolean> createEnabledOptionBuilderWithTitle(ModuleConfigBuilder builder) {
        return createEnabledOptionBuilderWithTitle(builder, true);
    }

    public static ModuleOptionBuilder<Boolean> createEnabledOptionBuilderWithTitle(
        ModuleConfigBuilder builder,
        boolean defaultValue
    ) {
        return builder.createBooleanOptionBuilder("enabled")
                      .setDefaultValue(defaultValue)
                      .setTitle(DEFAULT_ENABLED_TITLE);
    }

    public static ModuleOptionBuilder<ListMode> createListModeOptionBuilderWithTitle(
        ModuleConfigBuilder builder,
        ModuleOption<? extends List<?>> list,
        ListMode defaultValue
    ) {
        return builder
            .<ListMode>createEnumOptionBuilder(list.getOptionId() + "_list_mode")
            .setDefaultValue(defaultValue)
            .setTitle(createDefaultLangBuilder("list_mode")
                .translation("zh_cn", "%s - 列表模式")
                .translation("en_us", "%s - List Mode")
                .child(list.getTitle().getPlaceholder())
                .build()
            );
    }
}

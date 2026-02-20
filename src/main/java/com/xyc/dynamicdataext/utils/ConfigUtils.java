package com.xyc.dynamicdataext.utils;

import com.xyc.dynamicdataext.DynamicDataMain;
import com.xyc.dynamicdataext.base.NamedEnum;
import com.xyc.dynamicdataext.config.ModuleConfigBuilder;
import com.xyc.dynamicdataext.config.ModuleOption;
import com.xyc.dynamicdataext.config.ModuleOptionBuilder;
import com.xyc.dynamicdataext.lang.ModuleLang;
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

    public static final TranslatableLang DEFAULT_ID_FORMAT_INSTRUCTION =
        createDefaultLangBuilder("id_format_instruction")
            .translation(
                "zh_cn",
                """
                    %s
                      * 单项 - %s
                      * 标签 - %s
                      > 命名空间为“minecraft”时，前缀“minecraft:”可省略"""
            ).translation(
                "en_us",
                """
                    %s
                      * Single - %s
                      * Tag    - %s
                      > If the namespace is "minecraft", the prefix "minecraft:" can be omitted"""
            ).child(
                createDefaultLangBuilder("id_format_instruction", "tooltip_head")
                    .translation("zh_cn", "命名空间ID格式")
                    .translation("en_us", "Namespaced ID Formats")
                    .format(ChatFormatting.ITALIC, ChatFormatting.WHITE)
                    .build()
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
            "module",
            DynamicDataMain.MOD_ID,
            ArrayUtils.insert(0, id, "__default__")
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

    public static ModuleOptionBuilder<List<String>> createEntryListOptionBuilderWithTooltip(
        ModuleConfigBuilder builder,
        String optionId
    ) {
        return createEntryListOptionBuilderWithTooltip(builder, optionId, List.of());
    }

    public static ModuleOptionBuilder<List<String>> createEntryListOptionBuilderWithTooltip(
        ModuleConfigBuilder builder,
        String optionId,
        List<String> defaultValue
    ) {
        return builder.createStringListOptionBuilder(optionId)
                      .setDefaultValue(defaultValue)
                      .setTooltip(ConfigUtils.DEFAULT_ID_FORMAT_INSTRUCTION);
    }

    public enum ListMode implements NamedEnum {
        WHITELIST(DEFAULT_LIST_MODE_WHITELIST),
        BLACKLIST(DEFAULT_LIST_MODE_BLACKLIST);

        private final ModuleLang name;

        ListMode(ModuleLang name) {
            this.name = name;
        }

        @Override
        public ModuleLang getName() {
            return this.name;
        }
    }
}

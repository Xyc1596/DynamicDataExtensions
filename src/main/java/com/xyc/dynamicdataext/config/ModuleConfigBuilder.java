package com.xyc.dynamicdataext.config;

import com.xyc.dynamicdataext.DynamicDataMain;
import com.xyc.dynamicdataext.lang.ModuleLangBuilder;
import com.xyc.dynamicdataext.lang.TranslatableBuilder;
import com.xyc.dynamicdataext.lang.TranslatableLang;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;

public class ModuleConfigBuilder {
    public static final TranslatableLang DEFAULT_ENABLED_TITLE = ModuleLangBuilder
        .translatable("module", DynamicDataMain.MOD_ID, "default", "enabled")
        .translation("zh_cn", "启用模块")
        .translation("en_us", "Enable")
        .build();

    protected TranslatableLang title;
    protected final String namespace;
    protected final String moduleId;
    protected @Nullable ModuleBooleanOption enabled = null;
    protected final Map<String, ModuleOption<?>> options = new LinkedHashMap<>();
    protected final TranslatableBuilder titleLangBuilder;

    public ModuleConfigBuilder(String namespace, String moduleId) {
        this.namespace = namespace;
        this.moduleId = moduleId;
        this.titleLangBuilder = ModuleLangBuilder.translatable("module", namespace, moduleId);
    }

    public ModuleConfigBuilder defineEnabled(@NotNull ModuleOption<Boolean> option) {
        this.enabled = (ModuleBooleanOption) option;
        return this;
    }

    @SuppressWarnings("unused")
    public ModuleConfigBuilder defineOption(@NotNull ModuleOption<?> option) {
        String optionId = option.getOptionId();
        if (this.options.containsKey(optionId))
            throw new IllegalStateException("Duplicate option id " + optionId);
        this.options.put(optionId, option);
        return this;
    }

    public TranslatableBuilder getTitleLangBuilder() {
        return this.titleLangBuilder;
    }

    public ModuleConfigBuilder setTitle(TranslatableLang lang) {
        this.title = lang;
        return this;
    }

    public ModuleConfig build() {
        return new ModuleConfig(
            this.namespace,
            this.moduleId,
            this.title == null ? this.titleLangBuilder.build() : this.title,
            this.enabled,
            this.options
        );
    }

    public ModuleOptionBuilder<Boolean> createBooleanOptionBuilder(String optionId) {
        return new ModuleOptionBuilder<>(this.namespace, this.moduleId, optionId, ModuleBooleanOption::new);
    }

    public ModuleOptionBuilder<Boolean> createEnabledOptionBuilderWithDefaultTitle() {
        return this.createBooleanOptionBuilder("enabled").setTitle(DEFAULT_ENABLED_TITLE);
    }
}

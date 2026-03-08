package com.xyc.dynamicdataext.config;

import com.xyc.dynamicdataext.utils.NamedEnum;
import com.xyc.dynamicdataext.lang.ModuleLangBuilder;
import com.xyc.dynamicdataext.lang.TranslatableBuilder;
import com.xyc.dynamicdataext.lang.TranslatableLang;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ModuleConfigBuilder {
    protected TranslatableLang title;
    protected final String namespace;
    protected final String moduleId;
    protected @Nullable BooleanOption enabled = null;
    protected final Map<String, ModuleOption<?>> options = new LinkedHashMap<>();
    protected final TranslatableBuilder titleLangBuilder;

    public ModuleConfigBuilder(String namespace, String moduleId) {
        this.namespace = namespace;
        this.moduleId = moduleId;
        this.titleLangBuilder = ModuleLangBuilder.translatable("module", namespace, moduleId);
    }

    public ModuleConfigBuilder defineEnabled(@NotNull ModuleOption<Boolean> option) {
        this.enabled = (BooleanOption) option;
        return this;
    }

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
        return new ModuleOptionBuilder<>(this.namespace, this.moduleId, optionId, BooleanOption::new);
    }

    public ModuleOptionBuilder<List<String>> createStringListOptionBuilder(String optionId) {
        return new ModuleOptionBuilder<>(this.namespace, this.moduleId, optionId, StringListOption::new);
    }

    public <T extends Enum<T> & NamedEnum> ModuleOptionBuilder<T> createEnumOptionBuilder(String optionId) {
        return new ModuleOptionBuilder<>(this.namespace, this.moduleId, optionId, EnumOption<T>::new);
    }
}

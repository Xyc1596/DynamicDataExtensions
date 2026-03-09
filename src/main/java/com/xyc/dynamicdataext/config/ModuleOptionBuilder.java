package com.xyc.dynamicdataext.config;

import com.xyc.dynamicdataext.lang.ModuleLangBuilder;
import com.xyc.dynamicdataext.lang.TranslatableBuilder;
import com.xyc.dynamicdataext.lang.TranslatableLang;

public class ModuleOptionBuilder<T> {
    protected final String moduleId;
    protected final String optionId;
    protected TranslatableLang title;
    protected TranslatableLang tooltip;
    protected T defaultValue;
    protected final Factory<T> factory;
    protected final TranslatableBuilder<TranslatableLang> titleLangBuilder;
    protected final TranslatableBuilder<TranslatableLang> tooltipLangBuilder;

    protected ModuleOptionBuilder(
        String namespace,
        String moduleId,
        String optionId,
        Factory<T> factory
    ) {
        this.moduleId = moduleId;
        this.optionId = optionId;
        this.factory = factory;
        this.titleLangBuilder = ModuleLangBuilder.translatable(
            "module", namespace, moduleId, optionId, "title"
        );
        this.tooltipLangBuilder = ModuleLangBuilder.translatable(
            "module", namespace, moduleId, optionId, "tooltip"
        );
    }

    public ModuleOptionBuilder<T> setDefaultValue(T value) {
        this.defaultValue = value;
        return this;
    }

    public TranslatableBuilder<TranslatableLang> getTitleLangBuilder() {
        return this.titleLangBuilder;
    }

    @SuppressWarnings("unused")
    public TranslatableBuilder<TranslatableLang> createTitleChildLangBuilder(String childId) {
        return this.titleLangBuilder.childTranslatableBuilder(childId);
    }

    @SuppressWarnings("unused")
    public TranslatableBuilder<TranslatableLang> createTitleChildLangBuilder() {
        return this.titleLangBuilder.childTranslatableBuilder();
    }

    public ModuleOptionBuilder<T> setTitle(TranslatableLang lang) {
        this.title = lang;
        return this;
    }

    public TranslatableBuilder<TranslatableLang> getTooltipLangBuilder() {
        return this.tooltipLangBuilder;
    }

    public TranslatableBuilder<TranslatableLang> createTooltipChildLangBuilder(String childId) {
        return this.tooltipLangBuilder.childTranslatableBuilder(childId);
    }

    public TranslatableBuilder<TranslatableLang> createTooltipChildLangBuilder() {
        return this.tooltipLangBuilder.childTranslatableBuilder();
    }

    public ModuleOptionBuilder<T> setTooltip(TranslatableLang lang) {
        this.tooltip = lang;
        return this;
    }

    public ModuleOption<T> build() {
        if (this.defaultValue == null) {
            throw new IllegalStateException("Null default value in" + this.moduleId + "." + this.optionId);
        }
        return this.factory.create(
            this.optionId,
            this.title == null ? this.titleLangBuilder.build() : this.title,
            this.tooltip == null ? this.tooltipLangBuilder.build() : this.tooltip,
            this.defaultValue
        );
    }

    public interface Factory<T> {
        ModuleOption<T> create(String optionId, TranslatableLang title, TranslatableLang tooltip, T defaultValue);
    }
}

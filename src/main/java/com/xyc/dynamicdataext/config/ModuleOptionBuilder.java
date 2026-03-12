package com.xyc.dynamicdataext.config;

import com.xyc.dynamicdataext.lang.AbstractTranslatableLang;
import com.xyc.dynamicdataext.lang.ModuleLang;
import com.xyc.dynamicdataext.lang.TranslatableLang;

public class ModuleOptionBuilder<T> {
    protected final String moduleId;
    protected final String optionId;
    protected ModuleLang title;
    protected ModuleLang tooltip;
    protected T defaultValue;
    protected final Factory<T> factory;
    protected final AbstractTranslatableLang.Builder titleLangBuilder;
    protected final AbstractTranslatableLang.Builder tooltipLangBuilder;

    protected ModuleOptionBuilder(
        String namespace,
        String moduleId,
        String optionId,
        Factory<T> factory
    ) {
        this.moduleId = moduleId;
        this.optionId = optionId;
        this.factory = factory;
        this.titleLangBuilder = ModuleLang.translatable(
            "module", namespace, moduleId, optionId, "title"
        );
        this.tooltipLangBuilder = ModuleLang.translatable(
            "module", namespace, moduleId, optionId, "tooltip"
        );
    }

    public ModuleOptionBuilder<T> setDefaultValue(T value) {
        this.defaultValue = value;
        return this;
    }

    public TranslatableLang.Builder getTitleLangBuilder() {
        return this.titleLangBuilder;
    }

    @SuppressWarnings("unused")
    public TranslatableLang.Builder createTitleChildLangBuilder(String childId) {
        return this.titleLangBuilder.childTranslatableBuilder(childId);
    }

    @SuppressWarnings("unused")
    public TranslatableLang.Builder createTitleChildLangBuilder() {
        return this.titleLangBuilder.childTranslatableBuilder();
    }

    public ModuleOptionBuilder<T> setTitle(ModuleLang lang) {
        this.title = lang;
        return this;
    }

    public TranslatableLang.Builder getTooltipLangBuilder() {
        return this.tooltipLangBuilder;
    }

    @SuppressWarnings("unused")
    public TranslatableLang.Builder createTooltipChildLangBuilder(String childId) {
        return this.tooltipLangBuilder.childTranslatableBuilder(childId);
    }

    @SuppressWarnings("unused")
    public TranslatableLang.Builder createTooltipChildLangBuilder() {
        return this.tooltipLangBuilder.childTranslatableBuilder();
    }

    public ModuleOptionBuilder<T> setTooltip(ModuleLang lang) {
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
        ModuleOption<T> create(String optionId, ModuleLang title, ModuleLang tooltip, T defaultValue);
    }
}

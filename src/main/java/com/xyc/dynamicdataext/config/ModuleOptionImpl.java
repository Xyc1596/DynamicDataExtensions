package com.xyc.dynamicdataext.config;

import com.xyc.dynamicdataext.lang.TranslatableLang;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

public abstract class ModuleOptionImpl<T> implements ModuleOption<T> {
    protected final String optionId;
    protected final TranslatableLang title;
    protected final TranslatableLang tooltip;
    protected final T defaultValue;
    protected ModConfigSpec.ConfigValue<T> value;

    public ModuleOptionImpl(String optionId, TranslatableLang title, TranslatableLang tooltip, T defaultValue) {
        this.optionId = optionId;
        this.title = title;
        this.tooltip = tooltip;
        this.defaultValue = defaultValue;
    }

    @Override
    public final T getValue() {
        return this.value.get();
    }

    @Override
    public final void setValue(T value) {
        this.value.set(value);
    }

    @Override
    public final String getOptionId() {
        return this.optionId;
    }

    @Override
    public TranslatableLang getTitle() {
        return this.title;
    }

    @Override
    public final List<TranslatableLang> getAllTranslatableLang() {
        return List.of(this.title, this.tooltip);
    }
}

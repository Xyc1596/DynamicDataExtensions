package com.xyc.dynamicdataext.config;

import com.xyc.dynamicdataext.lang.ModuleLang;
import net.neoforged.neoforge.common.ModConfigSpec;

public abstract class ModuleOptionImpl<T> implements ModuleOption<T> {
    protected final String optionId;
    protected final ModuleLang title;
    protected final ModuleLang tooltip;
    protected final T defaultValue;
    protected ModConfigSpec.ConfigValue<T> value;

    public ModuleOptionImpl(String optionId, ModuleLang title, ModuleLang tooltip, T defaultValue) {
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
    public ModuleLang getTitle() {
        return this.title;
    }

}

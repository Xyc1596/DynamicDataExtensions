package com.xyc.dynamicdataext.config;

import com.xyc.dynamicdataext.lang.ModuleLang;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

public abstract class ModuleListOptionImpl<T> implements ModuleOption<List<T>> {
    protected final String optionId;
    protected final ModuleLang title;
    protected final ModuleLang tooltip;
    protected final List<T> defaultValue;
    protected ModConfigSpec.ConfigValue<List<? extends T>> value;

    public ModuleListOptionImpl(
        String optionId,
        ModuleLang title,
        ModuleLang tooltip,
        List<T> defaultValue
    ) {
        this.optionId = optionId;
        this.title = title;
        this.tooltip = tooltip;
        this.defaultValue = defaultValue;
    }

    @Override
    @SuppressWarnings("unchecked")
    public final List<T> getValue() {
        return (List<T>) this.value.get();
    }

    @Override
    public final void setValue(List<T> value) {
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

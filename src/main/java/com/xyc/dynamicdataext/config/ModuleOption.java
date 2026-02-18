package com.xyc.dynamicdataext.config;

import com.xyc.dynamicdataext.lang.TranslatableLang;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

public abstract class ModuleOption<T> {
    protected final String optionId;
    protected final TranslatableLang title;
    protected final TranslatableLang tooltip;
    protected final T defaultValue;
    protected ModConfigSpec.ConfigValue<T> value;

    public ModuleOption(String optionId, TranslatableLang title, TranslatableLang tooltip, T defaultValue) {
        this.optionId = optionId;
        this.title = title;
        this.tooltip = tooltip;
        this.defaultValue = defaultValue;
    }

    public abstract void buildSpec(ModConfigSpec.Builder builder);

    public abstract void buildCloth(ConfigCategory category, ConfigEntryBuilder entryBuilder);

    public final T getValue() {
        return this.value.get();
    }

    public final void setValue(T value) {
        this.value.set(value);
    }

    public final String getOptionId() {
        return this.optionId;
    }

    public final List<TranslatableLang> getAllTranslatableLang() {
        return List.of(this.title, this.tooltip);
    }
}

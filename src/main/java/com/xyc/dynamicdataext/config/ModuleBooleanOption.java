package com.xyc.dynamicdataext.config;

import com.xyc.dynamicdataext.lang.TranslatableLang;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.neoforged.neoforge.common.ModConfigSpec;

public class ModuleBooleanOption extends ModuleOption<Boolean> {
    public ModuleBooleanOption(
        String moduleId,
        TranslatableLang title,
        TranslatableLang tooltip,
        Boolean defaultValue
    ) {
        super(moduleId, title, tooltip, defaultValue);
    }

    @Override
    public void buildSpec(ModConfigSpec.Builder builder) {
        if (this.value == null) {
            this.value = builder.define(this.title.toString(), this.defaultValue);
        }
    }

    @Override
    public void buildCloth(ConfigCategory category, ConfigEntryBuilder entryBuilder) {
        category.addEntry(
            entryBuilder.startBooleanToggle(this.title.toComponent(), this.getValue())
                        .setTooltip(this.tooltip.toComponent())
                        .setSaveConsumer(this::setValue)
                        .setDefaultValue(this.defaultValue)
                        .build()
        );
    }


}

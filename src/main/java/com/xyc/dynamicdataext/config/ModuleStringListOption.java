package com.xyc.dynamicdataext.config;

import com.xyc.dynamicdataext.lang.TranslatableLang;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;
import java.util.Objects;

public class ModuleStringListOption extends ModuleListOptionImpl<String> {
    public ModuleStringListOption(
        String optionId,
        TranslatableLang title,
        TranslatableLang tooltip,
        List<String> defaultValue
    ) {
        super(optionId, title, tooltip, defaultValue);
    }

    @Override
    public void buildSpec(ModConfigSpec.Builder builder) {
        if (this.value == null) {
            this.value = builder.defineListAllowEmpty(
                this.optionId,
                this.defaultValue,
                () -> "",
                Objects::nonNull
            );
        }
    }

    @Override
    public void buildCloth(ConfigCategory category, ConfigEntryBuilder entryBuilder) {
        category.addEntry(
            entryBuilder.startStrList(this.title.toComponent(), this.getValue())
                        .setTooltip(this.tooltip.toComponent())
                        .setSaveConsumer(this::setValue)
                        .setDefaultValue(this.defaultValue)
                        .build()
        );
    }
}

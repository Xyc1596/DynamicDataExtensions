package com.xyc.dynamicdataext.config;

import com.xyc.dynamicdataext.utils.NamedEnum;
import com.xyc.dynamicdataext.lang.TranslatableLang;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.neoforged.neoforge.common.ModConfigSpec;

public class EnumOption<T extends Enum<T> & NamedEnum> extends ModuleOptionImpl<T> {
    public EnumOption(String optionId, TranslatableLang title, TranslatableLang tooltip, T defaultValue) {
        super(optionId, title, tooltip, defaultValue);
    }

    @Override
    public void buildSpec(ModConfigSpec.Builder builder) {
        if (this.value == null) {
            this.value = builder.defineEnum(this.optionId, this.defaultValue);
        }
    }

    @Override
    public void buildCloth(ConfigCategory category, ConfigEntryBuilder entryBuilder) {
        Class<T> enumClass = this.defaultValue.getDeclaringClass();
        category.addEntry(
            entryBuilder
                .startEnumSelector(this.title.toComponent(), enumClass, this.getValue())
                .setEnumNameProvider(e -> enumClass.cast(e).getName().toComponent())
                .setTooltip(this.tooltip.toComponent())
                .setSaveConsumer(this::setValue)
                .setDefaultValue(this.defaultValue)
                .build()
        );
    }
}

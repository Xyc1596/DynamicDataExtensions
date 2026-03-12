package com.xyc.dynamicdataext.config;

import com.xyc.dynamicdataext.lang.ModuleLang;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

public interface ModuleOption<T> {
    void buildSpec(ModConfigSpec.Builder builder);

    void buildCloth(ConfigCategory category, ConfigEntryBuilder entryBuilder);

    T getValue();

    void setValue(T value);

    String getOptionId();

    ModuleLang getTitle();

    List<ModuleLang> getAllModuleLang();
}

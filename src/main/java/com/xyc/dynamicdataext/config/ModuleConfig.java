package com.xyc.dynamicdataext.config;

import com.xyc.dynamicdataext.lang.TranslatableLang;
import com.xyc.dynamicdataext.utils.ConfigUtils;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.neoforged.neoforge.common.ModConfigSpec;

import javax.annotation.Nullable;
import java.util.*;

public class ModuleConfig {
    protected final TranslatableLang title;
    protected final String namespace;
    protected final String moduleId;
    protected final @Nullable ModuleBooleanOption enabled;    // 为null时始终生效，不可关闭
    protected final Map<String, ModuleOption<?>> allOptions;

    public ModuleConfig(
        String namespace,
        String moduleId,
        TranslatableLang title,
        @Nullable ModuleBooleanOption enabled,
        Map<String, ModuleOption<?>> options
    ) {
        this.namespace = namespace;
        this.moduleId = moduleId;
        this.title = title;
        this.enabled = enabled;
        this.allOptions = new LinkedHashMap<>();
        if (enabled != null) {
            this.allOptions.put(enabled.getOptionId(), enabled);
        }
        this.allOptions.putAll(options);
    }

    public void buildSpec(ModConfigSpec.Builder builder) {
        Collection<ModuleOption<?>> allOptions = this.allOptions.values();
        if (!allOptions.isEmpty()) {
            builder.push(this.moduleId);
            for (ModuleOption<?> option : this.allOptions.values()) {
                option.buildSpec(builder);
            }
            builder.pop();
        }
    }

    public void buildCloth(ConfigBuilder builder, ConfigEntryBuilder entryBuilder) {
        Collection<ModuleOption<?>> allOptions = this.allOptions.values();
        if (!allOptions.isEmpty()) {
            ConfigCategory category = builder.getOrCreateCategory(this.title.toComponent());
            for (ModuleOption<?> option : this.allOptions.values()) {
                option.buildCloth(category, entryBuilder);
            }
        }
    }

    public final List<TranslatableLang> getAllTranslatableLang() {
        List<TranslatableLang> output = new ArrayList<>();
        output.add(this.title);
        for (ModuleOption<?> option : this.allOptions.values()) {
            output.addAll(option.getAllTranslatableLang());
        }
        return output;
    }

    public boolean isEnabled() {
        return enabled == null || enabled.getValue();
    }

    public ModuleOption<?> getOption(String optionId) {
        return this.allOptions.get(optionId);
    }

    public static ModuleConfig defaultConfig(String namespace, String moduleId) {
        return new ModuleConfig(
            namespace,
            moduleId,
            TranslatableLang.empty("module", namespace, moduleId),
            null,
            Map.of()
        );
    }

    @SuppressWarnings("unused")
    public static ModuleConfig defaultConfigWithEnabled(String namespace, String moduleId, boolean defaultEnabled) {
        ModuleConfigBuilder builder = new ModuleConfigBuilder(namespace, moduleId);
        ModuleOptionBuilder<Boolean> enabled = ConfigUtils.createEnabledOptionBuilder(builder);
        return builder.defineEnabled(enabled.build()).build();
    }
}

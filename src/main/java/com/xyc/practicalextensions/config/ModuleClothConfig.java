package com.xyc.practicalextensions.config;

import com.xyc.practicalextensions.modules.IModule;
import com.xyc.practicalextensions.utils.Utils;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.network.chat.Component;
import net.neoforged.fml.IExtensionPoint;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

import java.util.Set;

public class ModuleClothConfig implements IExtensionPoint {
    public static final String
        TITLE_CONFIG = Utils.translateKey("title", "config"),
        TITLE_MODULES = Utils.translateKey("title", "modules");

    public static ConfigBuilder getBuilder(Set<IModule> modules) {
        ConfigBuilder builder = ConfigBuilder.create()
                                             .setTitle(Component.translatable(TITLE_CONFIG))
                                             .setSavingRunnable(ModuleConfig.getSpec()::save);
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        ConfigCategory categoryBuilder = builder.getOrCreateCategory(Component.translatable(TITLE_MODULES));
        modules.forEach(m -> categoryBuilder.addEntry(
            entryBuilder
                .startBooleanToggle(
                    Component.translatable(m.getOptionTranslateKey()),
                    ModuleConfig.isModuleEnabled(m.getId())
                )
                .setTooltip(Component.translatable(m.getTooltipTranslateKey()))
                .setSaveConsumer(newVal -> ModuleConfig.setModuleEnabled(m.getId(), newVal))
                .setDefaultValue(true)
                .build()
        ));
        return builder;
    }

    public static void init(ModContainer container, Set<IModule> modules) {
        container.registerExtensionPoint(
            IConfigScreenFactory.class,
            (c, s) -> getBuilder(modules).build()
        );
    }
}

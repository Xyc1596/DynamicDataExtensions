package com.xyc.practicalextensions.client;

import com.xyc.practicalextensions.ModuleConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.IExtensionPoint;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@OnlyIn(Dist.CLIENT)
public class ModuleClothConfig implements IExtensionPoint {
    public final String TITLE_CONFIG, TITLE_MODULES;

    public ConfigBuilder getBuilder(ModuleConfig moduleConfig) {
        ConfigBuilder builder = ConfigBuilder.create()
                                             .setTitle(Component.translatable(TITLE_CONFIG))
                                             .setSavingRunnable(moduleConfig.COMMON::save);
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        ConfigCategory categoryBuilder = builder.getOrCreateCategory(Component.translatable(TITLE_MODULES));
        moduleConfig.MODULES.forEach(m -> categoryBuilder.addEntry(
            entryBuilder
                .startBooleanToggle(
                    Component.translatable(m.getOptionTranslationKey()),
                    moduleConfig.isModuleEnabled(m.ID)
                )
                .setTooltip(Component.translatable(m.getTooltipTranslationKey()))
                .setSaveConsumer(newVal -> moduleConfig.setModuleEnabled(m.ID, newVal))
                .setDefaultValue(true)
                .build()
        ));
        return builder;
    }

    public ModuleClothConfig(String namespace, ModContainer container, ModuleConfig moduleConfig) {
        TITLE_CONFIG = "title." + namespace + ".config";
        TITLE_MODULES = "title." + namespace + ".modules";
        container.registerExtensionPoint(
            IConfigScreenFactory.class,
            (c, s) -> getBuilder(moduleConfig).build()
        );
    }
}

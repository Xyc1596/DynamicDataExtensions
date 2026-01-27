package com.xyc.practicalextensions;

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
    public final String TITLE_CONFIG;
    public static final String
        TITLE_MODULES = "title." + ModMain.MOD_ID + ".modules",
        TITLE_GENERAL = "title." + ModMain.MOD_ID + ".general",
        OPTION_AUTO_RELOAD = "option." + ModMain.MOD_ID + ".auto_reload",
        TOOLTIP_AUTO_RELOAD = "tooltip." + ModMain.MOD_ID + ".auto_reload";

    public ConfigBuilder getBuilder(ModuleConfig moduleConfig) {
        ConfigBuilder builder = ConfigBuilder.create()
                                             .setTitle(Component.translatable(TITLE_CONFIG))
                                             .setSavingRunnable(moduleConfig.COMMON::save);
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        ConfigCategory modulesBuilder = builder.getOrCreateCategory(Component.translatable(TITLE_MODULES));
        moduleConfig.MODULES.forEach(m -> modulesBuilder.addEntry(
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

        ConfigCategory generalBuilder = builder.getOrCreateCategory(Component.translatable(TITLE_GENERAL));
        generalBuilder.addEntry(
            entryBuilder
                .startBooleanToggle(Component.translatable(OPTION_AUTO_RELOAD), moduleConfig.AUTO_RELOAD.get())
                .setTooltip(Component.translatable(TOOLTIP_AUTO_RELOAD))
                .setSaveConsumer(moduleConfig.AUTO_RELOAD::set)
                .setDefaultValue(true)
                .build()
        );

        return builder;
    }

    public ModuleClothConfig(String namespace, ModContainer container, ModuleConfig moduleConfig) {
        TITLE_CONFIG = "title." + namespace + ".config";
        container.registerExtensionPoint(
            IConfigScreenFactory.class,
            (c, s) -> getBuilder(moduleConfig).build()
        );
    }
}

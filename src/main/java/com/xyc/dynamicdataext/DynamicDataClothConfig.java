package com.xyc.dynamicdataext;

import com.xyc.dynamicdataext.base.Module;
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
public class DynamicDataClothConfig implements IExtensionPoint {
    public final String TITLE_CONFIG;
    public static final String
        TITLE_MODULES = "title." + DynamicDataMain.MOD_ID + ".modules",
        TITLE_GENERAL = "title." + DynamicDataMain.MOD_ID + ".general",
        OPTION_AUTO_RELOAD = "option." + DynamicDataMain.MOD_ID + ".auto_reload",
        TOOLTIP_AUTO_RELOAD = "tooltip." + DynamicDataMain.MOD_ID + ".auto_reload";

    public ConfigBuilder getBuilder(DynamicDataConfig ddConfig) {
        ConfigBuilder builder = ConfigBuilder.create()
                                             .setTitle(Component.translatable(TITLE_CONFIG))
                                             .setSavingRunnable(ddConfig.COMMON::save);
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        ConfigCategory modulesBuilder = builder.getOrCreateCategory(Component.translatable(TITLE_MODULES));
        for (Module module : ddConfig.getModules()) {
            modulesBuilder.addEntry(
                entryBuilder
                    .startBooleanToggle(module.getOption().toComponent(), ddConfig.isModuleEnabled(module.getId()))
                    .setTooltip(module.getTooltip().toComponent())
                    .setSaveConsumer(newVal -> ddConfig.setModuleEnabled(module.getId(), newVal))
                    .setDefaultValue(true)
                    .build()
            );
        }

        ConfigCategory generalBuilder = builder.getOrCreateCategory(Component.translatable(TITLE_GENERAL));
        generalBuilder.addEntry(
            entryBuilder
                .startBooleanToggle(Component.translatable(OPTION_AUTO_RELOAD), ddConfig.AUTO_RELOAD.get())
                .setTooltip(Component.translatable(TOOLTIP_AUTO_RELOAD))
                .setSaveConsumer(ddConfig.AUTO_RELOAD::set)
                .setDefaultValue(true)
                .build()
        );

        return builder;
    }

    public DynamicDataClothConfig(String namespace, ModContainer container, DynamicDataConfig ddConfig) {
        TITLE_CONFIG = "title." + namespace + ".config";
        container.registerExtensionPoint(
            IConfigScreenFactory.class,
            (c, s) -> getBuilder(ddConfig).build()
        );
    }
}

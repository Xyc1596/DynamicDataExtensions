package com.xyc.dynamicdataext;

import com.xyc.dynamicdataext.base.Module;
import com.xyc.dynamicdataext.lang.ModuleLang;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.IExtensionPoint;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@OnlyIn(Dist.CLIENT)
public class DynamicDataClothConfig implements IExtensionPoint {
    public DynamicDataClothConfig(ModContainer container, DynamicDataConfig ddConfig, ModuleLang title) {
        container.registerExtensionPoint(
            IConfigScreenFactory.class,
            (c, s) -> this.getBuilder(ddConfig, title).setParentScreen(s).build()
        );
    }

    protected ConfigBuilder getBuilder(DynamicDataConfig ddConfig, ModuleLang title) {
        ConfigBuilder builder = ConfigBuilder.create()
                                             .setTitle(title.toComponent())
                                             .setSavingRunnable(ddConfig.COMMON::save);
        builder.setGlobalized(true);
        builder.setGlobalizedExpanded(true);
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        for (Module module : ddConfig.getModules()) {
            module.getConfig().buildCloth(builder, entryBuilder);
        }
        return builder;
    }
}

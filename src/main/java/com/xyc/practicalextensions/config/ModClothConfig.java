package com.xyc.practicalextensions.config;

import com.xyc.practicalextensions.ModMain;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.BooleanListEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.fml.IExtensionPoint;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

import java.util.function.Consumer;

public class ModClothConfig implements IExtensionPoint {
    public static ConfigBuilder getConfigBuilder() {
        ConfigBuilder builder = ConfigBuilder
            .create()
            .setTitle(Component.translatable("title." + ModMain.MOD_ID + ".config"))
            .setSavingRunnable(ModConfig::save);
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        ConfigCategory modules = builder.getOrCreateCategory(createTranslatable("title", "modules"));
        modules.addEntry(createBooleanEntry(
            entryBuilder,
            "leather_from_rotten_flesh",
            ModConfig.leatherFromRottenFlesh,
            n -> ModConfig.leatherFromRottenFlesh = n,
            true
        ));
        return builder;
    }

    public static void register() {
        ModMain.CONTAINER.registerExtensionPoint(IConfigScreenFactory.class, (c, s) -> getScreen());
    }

    public static Screen getScreen() {
        return getConfigBuilder().build();
    }

    protected static Component createTranslatable(String category, String id) {
        return Component.translatable(category + "." + ModMain.MOD_ID + "." + id);
    }

    @SuppressWarnings("SameParameterValue")
    protected static BooleanListEntry createBooleanEntry(
        ConfigEntryBuilder builder,
        String id,
        boolean field,
        Consumer<Boolean> saveConsumer,
        boolean defaultValue
    ) {
        return builder.startBooleanToggle(createTranslatable("option", id), field)
                      .setTooltip(createTranslatable("tooltip", id))
                      .setSaveConsumer(saveConsumer)
                      .setDefaultValue(defaultValue)
                      .build();
    }
}

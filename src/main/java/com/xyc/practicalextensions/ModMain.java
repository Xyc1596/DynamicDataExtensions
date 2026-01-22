package com.xyc.practicalextensions;

import com.mojang.logging.LogUtils;
import com.xyc.practicalextensions.config.ModConfig;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(ModMain.MOD_ID)
public class ModMain {
    public static final String MOD_ID = "practicalextensions";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static ModContainer CONTAINER;

    public ModMain(IEventBus modEventBus, ModContainer container) {
        ModMain.CONTAINER = container;
        container.registerConfig(net.neoforged.fml.config.ModConfig.Type.COMMON, ModConfig.COMMON);
    }

    public static ResourceLocation resourceLocation(String id) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, id);
    }
}

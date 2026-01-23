package com.xyc.practicalextensions;

import com.mojang.logging.LogUtils;
import com.xyc.practicalextensions.config.ModuleClothConfig;
import com.xyc.practicalextensions.config.ModuleConfig;
import com.xyc.practicalextensions.modules.ModuleManager;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import org.slf4j.Logger;

@Mod(ModMain.MOD_ID)
public class ModMain {
    public static final String MOD_ID = "practicalextensions";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static ModContainer CONTAINER;

    public ModMain(IEventBus modEventBus, ModContainer container) {
        ModMain.CONTAINER = container;
        ModuleConfig.init(container, ModuleManager.getModules());

        modEventBus.addListener((final InterModEnqueueEvent event) -> {
            if (FMLEnvironment.dist == Dist.CLIENT && ModList.get().isLoaded("cloth_config")) {
                event.enqueueWork(() -> ModuleClothConfig.init(CONTAINER, ModuleManager.getModules()));
            }
        });
    }
}

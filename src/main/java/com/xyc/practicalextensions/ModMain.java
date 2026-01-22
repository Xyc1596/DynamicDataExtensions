package com.xyc.practicalextensions;

import com.mojang.logging.LogUtils;
import com.xyc.practicalextensions.config.ModuleClothConfig;
import com.xyc.practicalextensions.config.ModuleConfig;
import com.xyc.practicalextensions.modules.ModuleManager;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import org.slf4j.Logger;

@Mod(ModMain.MOD_ID)
public class ModMain {
    public static final String MOD_ID = "practicalextensions";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static ModContainer CONTAINER;

    public ModMain(ModContainer container) {
        ModMain.CONTAINER = container;
        ModuleConfig.init(container, ModuleManager.getModules());
    }

    @OnlyIn(Dist.CLIENT)
    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD)
    public static final class ClientSetupEvents {
        @SubscribeEvent
        public static void onEnqueue(final InterModEnqueueEvent event) {
            if (ModList.get().isLoaded("cloth_config")) {
                event.enqueueWork(() -> ModuleClothConfig.init(CONTAINER, ModuleManager.getModules()));
            }
        }
    }

}

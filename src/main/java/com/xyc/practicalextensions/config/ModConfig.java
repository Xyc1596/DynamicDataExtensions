package com.xyc.practicalextensions.config;

import com.xyc.practicalextensions.ModMain;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

@EventBusSubscriber(modid = ModMain.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModConfig {
    public static final String MESSAGE_RELOAD_CONFIG = "message." + ModMain.MOD_ID + ".reload_config";

    private static final ModConfigSpec.Builder COMMON_BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue LEATHER_FROM_ROTTEN_FLESH = COMMON_BUILDER
        .define("LeatherFromRottenFlesh", true);
    public static boolean leatherFromRottenFlesh;

    public static final ModConfigSpec COMMON = COMMON_BUILDER.build();

    public static void load() {
        leatherFromRottenFlesh = LEATHER_FROM_ROTTEN_FLESH.get();
    }

    public static void save() {
        LEATHER_FROM_ROTTEN_FLESH.set(leatherFromRottenFlesh);
        COMMON.save();
    }

    @SubscribeEvent
    public static void onLoadConfig(final ModConfigEvent event) {
        load();
    }

    @SubscribeEvent
    public static void onReloadConfig(final ModConfigEvent.Reloading event) {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server != null) {
            server.reloadResources(server.getPackRepository().getSelectedIds());
            server.getPlayerList().broadcastSystemMessage(
                Component.translatable(
                    MESSAGE_RELOAD_CONFIG,
                    Component.literal(ModMain.MOD_ID).withStyle(ChatFormatting.DARK_AQUA)
                ),
                false
            );
        }
    }

    @SubscribeEvent
    public static void onEnqueue(final InterModEnqueueEvent event) {
        if (FMLEnvironment.dist == Dist.CLIENT && ModList.get().isLoaded("cloth_config")) {
            event.enqueueWork(ModClothConfig::register);
        }
    }
}

package com.xyc.practicalextensions.config;

import com.xyc.practicalextensions.ModMain;
import com.xyc.practicalextensions.modules.IModule;
import com.xyc.practicalextensions.utils.Utils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.config.IConfigSpec;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@EventBusSubscriber(modid = ModMain.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModuleConfig {
    public static final String MESSAGE_RELOAD_CONFIG = Utils.translateKey("message", "reload_config");

    private static int cache;

    protected static ModConfigSpec COMMON;

    protected static final Map<String, ModConfigSpec.BooleanValue> MODULE_TOGGLES = new LinkedHashMap<>();

    public static ModConfigSpec getSpec() {
        return COMMON;
    }

    public static boolean isModuleEnabled(String id) {
        return MODULE_TOGGLES.get(id).get();
    }

    public static void setModuleEnabled(String id, boolean enabled) {
        MODULE_TOGGLES.get(id).set(enabled);
    }

    public static void init(ModContainer container, Set<IModule> modules) {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        modules.forEach(m -> MODULE_TOGGLES.put(
            m.getId(), builder.define(m.getId(), true)
        ));
        COMMON = builder.build();
        container.registerConfig(ModConfig.Type.COMMON, COMMON);
    }

    @SubscribeEvent
    public static void onLoadConfig(final ModConfigEvent.Loading event) {
        IConfigSpec.ILoadedConfig loadedConfig = event.getConfig().getLoadedConfig();
        if (loadedConfig != null){
            cache = loadedConfig.hashCode();
        }
    }

    @SubscribeEvent
    public static void onReloadConfig(final ModConfigEvent.Reloading event) {
        IConfigSpec.ILoadedConfig loadedConfig = event.getConfig().getLoadedConfig();
        if (loadedConfig == null)
            return;

        // ModuleClothConfig 调用 ModConfigSpec#save() 导致 ModConfigEvent.Reloading 事件多触发一次
        // 将当前 LoadedConfig 的 HashCode 与缓存值比较以滤除重复事件
        int newHash = loadedConfig.hashCode();
        if (newHash == cache)
            return;

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
            cache = newHash;
        }
    }
}

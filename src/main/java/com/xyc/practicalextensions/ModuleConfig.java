package com.xyc.practicalextensions;

import com.xyc.practicalextensions.base.Module;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.IConfigSpec;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ModuleConfig {
    public final static String
        MESSAGE_RELOAD_CONFIG = "message." + ModMain.MOD_ID + ".reload_config",
        MESSAGE_AUTO_RELOAD_DISABLED = "message." + ModMain.MOD_ID + ".auto_reload_disabled",
        MESSAGE_NO_PERMISSION = "message." + ModMain.MOD_ID + ".no_permission";

    private int cache;

    public final ModConfigSpec COMMON;

    public final ModConfigSpec.BooleanValue AUTO_RELOAD;

    private final List<Module> modules;
    private final Map<String, ModConfigSpec.BooleanValue> moduleToggles = new LinkedHashMap<>();

    public List<Module> getModules() {
        return modules;
    }

    public final boolean isModuleEnabled(String id) {
        return moduleToggles.get(id).get();
    }

    public final void setModuleEnabled(String id, boolean enabled) {
        moduleToggles.get(id).set(enabled);
    }

    public ModuleConfig(IEventBus modEventBus, ModContainer container, List<Module> modules) {
        this.modules = modules.stream().distinct().toList();

        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.push("Modules");
        modules.forEach(m -> moduleToggles.put(
            m.getId(), builder.define(m.getId(), true)
        ));

        builder.push("General");
        AUTO_RELOAD = builder.define("autoReload", true);

        COMMON = builder.build();
        container.registerConfig(ModConfig.Type.COMMON, COMMON);

        modEventBus.addListener(this::handleLoadConfig);
        modEventBus.addListener(this::handleReloadConfig);
    }

    public void handleLoadConfig(final ModConfigEvent.Loading event) {
        IConfigSpec.ILoadedConfig loadedConfig = event.getConfig().getLoadedConfig();
        if (loadedConfig != null) {
            cache = loadedConfig.hashCode();
        }
    }

    public void handleReloadConfig(final ModConfigEvent.Reloading event) {
        IConfigSpec.ILoadedConfig loadedConfig = event.getConfig().getLoadedConfig();
        if (loadedConfig == null)
            return;

        // ModuleClothConfig 调用 ModConfigSpec#save() 导致 ModConfigEvent.Reloading 事件多触发一次
        // 将当前 LoadedConfig 的 HashCode 与缓存值比较以滤除重复事件
        int newHash = loadedConfig.hashCode();
        if (newHash == cache)
            return;

        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null)
            return;
        PlayerList playerList = server.getPlayerList();

        if (FMLEnvironment.dist == Dist.CLIENT) {
            LocalPlayer localPlayer = Minecraft.getInstance().player;
            if (localPlayer == null)
                return;

            ServerPlayer player = playerList.getPlayer(localPlayer.getUUID());
            if (player == null)
                return;

            if (!playerList.isOp(player.getGameProfile())) {
                player.sendSystemMessage(
                    Component.translatable(
                        MESSAGE_NO_PERMISSION,
                        Component.translatable(ModMain.MOD_ID)
                                 .withStyle(ChatFormatting.DARK_AQUA)
                                 .withStyle(ChatFormatting.BOLD)
                    ).withStyle(ChatFormatting.RED)
                );
                return;
            }
        }

        if (AUTO_RELOAD.get()) {
            server.reloadResources(server.getPackRepository().getSelectedIds());
            server.getPlayerList().broadcastSystemMessage(
                Component.translatable(
                    MESSAGE_RELOAD_CONFIG,
                    Component.translatable(ModMain.MOD_ID)
                             .withStyle(ChatFormatting.DARK_AQUA)
                             .withStyle(ChatFormatting.BOLD)
                ),
                false
            );
        } else {
            for (ServerPlayer player : playerList.getPlayers()) {
                if (playerList.isOp(player.getGameProfile())) {
                    player.sendSystemMessage(
                        Component.translatable(
                            MESSAGE_AUTO_RELOAD_DISABLED,
                            Component.translatable(ModMain.MOD_ID)
                                     .withStyle(ChatFormatting.DARK_AQUA)
                                     .withStyle(ChatFormatting.BOLD)
                        )
                    );
                }
            }
        }
        cache = newHash;
    }
}

package com.xyc.dynamicdataext.utils;

import com.xyc.dynamicdataext.lang.ModuleLangBuilder;
import com.xyc.dynamicdataext.lang.TranslatableBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

public final class ModMainUtils {
    public static TranslatableBuilder createModTitleBuilder(String namespace) {
        return ModuleLangBuilder.translatable(null, namespace);
    }

    public static void broadcastMessage(Component component) {
        broadcastMessage(component, false);
    }

    public static void broadcastMessage(Component component, boolean opOnly) {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server != null) {
            PlayerList playerList = server.getPlayerList();
            if (opOnly) {
                for (ServerPlayer player : playerList.getPlayers())
                    if (playerList.isOp(player.getGameProfile()))
                        player.sendSystemMessage(component);
            } else server.getPlayerList().broadcastSystemMessage(component, false);
            server.sendSystemMessage(component);
        }
    }
}

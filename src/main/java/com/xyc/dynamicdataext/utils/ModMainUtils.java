package com.xyc.dynamicdataext.utils;

import com.xyc.dynamicdataext.lang.ModuleLangBuilder;
import com.xyc.dynamicdataext.lang.TranslatableBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.function.Consumer;

public final class ModMainUtils {
    public static TranslatableBuilder createModTitleBuilder(String namespace) {
        return ModuleLangBuilder.translatable(null, namespace);
    }

    public static void broadcastMessage(Component component, Consumer<String> logPrinter) {
        broadcastMessage(component, false, logPrinter);
    }

    public static void broadcastMessage(Component component, boolean opOnly, Consumer<String> logPrinter) {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server != null) {
            PlayerList playerList = server.getPlayerList();
            for (ServerPlayer player : playerList.getPlayers())
                if (!opOnly || playerList.isOp(player.getGameProfile()))
                    player.sendSystemMessage(component, true);
        }
        logPrinter.accept(component.getString());
    }
}

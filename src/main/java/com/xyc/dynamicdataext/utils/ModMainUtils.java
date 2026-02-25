package com.xyc.dynamicdataext.utils;

import com.xyc.dynamicdataext.lang.ModuleLangBuilder;
import com.xyc.dynamicdataext.lang.TranslatableBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

public final class ModMainUtils {
    public static TranslatableBuilder createModTitleBuilder(String namespace) {
        return ModuleLangBuilder.translatable(null, namespace);
    }

    public static void broadcastMessage(Component component) {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server != null) {
            server.getPlayerList().broadcastSystemMessage(component, false);
            server.sendSystemMessage(component);
        }
    }
}

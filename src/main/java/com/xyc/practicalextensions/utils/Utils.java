package com.xyc.practicalextensions.utils;

import com.xyc.practicalextensions.ModMain;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

@SuppressWarnings("unused")
public final class Utils {
    public static ResourceLocation resourceLocation(String id) {
        return ResourceLocation.fromNamespaceAndPath(ModMain.MOD_ID, id);
    }

    public static String translateKey(String category, String id) {
        return category + "." + ModMain.MOD_ID + "." + id;
    }

    public static Component translatable(String category, String id) {
        return Component.translatable(translateKey(category, id));
    }
}

package com.xyc.dynamicdataext.utils;

import com.xyc.dynamicdataext.base.Module;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import javax.annotation.ParametersAreNonnullByDefault;

@SuppressWarnings("unused")
public final class LocationUtils {
    @ParametersAreNonnullByDefault
    public static ResourceLocation withDefaultNamespace(String... paths) {
        return ResourceLocation.withDefaultNamespace(String.join(".", paths));
    }

    @ParametersAreNonnullByDefault
    public static ResourceLocation fromNamespaceAndPath(String namespace, String... paths) {
        return ResourceLocation.fromNamespaceAndPath(namespace, String.join(".", paths));
    }

    @ParametersAreNonnullByDefault
    public static ResourceLocation withCommonNamespace(String... paths) {
        return LocationUtils.fromNamespaceAndPath("c", String.join(".", paths));
    }

    @ParametersAreNonnullByDefault
    public static String createContentNameWithModuleId(Module module, String id) {
        return module.getModuleId() + "." + id;
    }

    @ParametersAreNonnullByDefault
    public static ResourceLocation createContentLocationWithModuleId(Module module, String id) {
        return LocationUtils.fromNamespaceAndPath(
            module.getNamespace(),
            createContentNameWithModuleId(module, id)
        );
    }

    @ParametersAreNonnullByDefault
    public static String getItemId(Item item) {
        return BuiltInRegistries.ITEM.getKey(item).getPath();
    }
}

package com.xyc.dynamicdataext.utils;

import com.xyc.dynamicdataext.base.Module;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;

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
    public static String createNameWithModuleId(Module module, String id) {
        return module.getModuleId() + "." + id;
    }

    @ParametersAreNonnullByDefault
    public static ResourceLocation createLocationWithModuleId(Module module, String id) {
        return LocationUtils.fromNamespaceAndPath(
            module.getNamespace(),
            createNameWithModuleId(module, id)
        );
    }
}

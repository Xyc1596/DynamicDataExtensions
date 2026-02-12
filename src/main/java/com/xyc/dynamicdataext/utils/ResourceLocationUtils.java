package com.xyc.dynamicdataext.utils;

import com.xyc.dynamicdataext.base.Module;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import javax.annotation.ParametersAreNonnullByDefault;

@SuppressWarnings("unused")
public final class ResourceLocationUtils {
    @ParametersAreNonnullByDefault
    public static ResourceLocation withDefaultNamespace(String id) {
        return ResourceLocation.withDefaultNamespace(id);
    }

    @ParametersAreNonnullByDefault
    public static ResourceLocation fromNamespaceAndPath(String namespace, String path) {
        return ResourceLocation.fromNamespaceAndPath(namespace, path);
    }

    @ParametersAreNonnullByDefault
    public static ResourceLocation withCommonNamespace(String id) {
        return ResourceLocationUtils.fromNamespaceAndPath("c", id);
    }

    @ParametersAreNonnullByDefault
    public static TagKey<Item> createItemTagKey(ResourceLocation location) {
        return TagKey.create(Registries.ITEM, location);
    }

    @ParametersAreNonnullByDefault
    public static TagKey<Item> createItemTagKey(String namespace, String path) {
        return TagKey.create(Registries.ITEM, ResourceLocationUtils.fromNamespaceAndPath(namespace, path));
    }

    @ParametersAreNonnullByDefault
    public static String getContentNameWithModuleId(Module module, String id) {
        return module.getId() + "." + id;
    }

    @ParametersAreNonnullByDefault
    public static ResourceLocation getContentLocationWithModuleId(Module module, String id) {
        return ResourceLocationUtils.fromNamespaceAndPath(
            module.getNamespace(),
            getContentNameWithModuleId(module, id)
        );
    }
}

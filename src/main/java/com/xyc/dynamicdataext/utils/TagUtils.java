package com.xyc.dynamicdataext.utils;

import com.xyc.dynamicdataext.base.DynamicTagEntry;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public final class TagUtils {
    @ParametersAreNonnullByDefault
    public static TagKey<Item> createItemTag(ResourceLocation location) {
        return TagKey.create(Registries.ITEM, location);
    }

    @ParametersAreNonnullByDefault
    public static TagKey<Item> createItemTag(String namespace, String... paths) {
        return TagKey.create(Registries.ITEM, LocationUtils.fromNamespaceAndPath(namespace, paths));
    }

    @ParametersAreNonnullByDefault
    public static <T> Set<T> getHolderSetContents(HolderSet<T> holders) {
        Set<T> output = new LinkedHashSet<>();
        holders.forEach(holder -> output.add(holder.value()));
        return output;
    }

    @ParametersAreNonnullByDefault
    public static Holder<Item> getItemHolder(Item item) {
        return item.getDefaultInstance().getItemHolder();
    }

    @ParametersAreNonnullByDefault
    public static DynamicTagEntry<Item> createItemTagEntry(TagKey<Item> tag, Collection<Item> contents) {
        return createItemTagEntry(tag, contents.toArray(new Item[0]));
    }

    @ParametersAreNonnullByDefault
    public static DynamicTagEntry<Item> createItemTagEntry(TagKey<Item> tag, Item... contents) {
        Set<Holder<Item>> holders = new LinkedHashSet<>();
        for (Item item : contents)
            holders.add(getItemHolder(item));
        return DynamicTagEntry.itemTag(tag, holders);
    }

}

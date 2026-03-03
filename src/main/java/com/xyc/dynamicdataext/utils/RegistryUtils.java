package com.xyc.dynamicdataext.utils;

import com.xyc.dynamicdataext.base.DynamicTagEntry;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

public final class RegistryUtils {
    public static final DefaultedRegistry<Item> ITEM_REGISTRY = BuiltInRegistries.ITEM;

    @ParametersAreNonnullByDefault
    public static TagKey<Item> createItemTag(ResourceLocation location) {
        return TagKey.create(Registries.ITEM, location);
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
    @SuppressWarnings("unused")
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

    @ParametersAreNonnullByDefault
    public static Optional<HolderSet.Named<Item>> getItemTagContents(ResourceLocation location) {
        return ITEM_REGISTRY.getTag(createItemTag(location));
    }

    @ParametersAreNonnullByDefault
    public static Optional<HolderSet.Named<Item>> getItemTagContents(TagKey<Item> tag) {
        return ITEM_REGISTRY.getTag(tag);
    }

    @ParametersAreNonnullByDefault
    public static String getItemId(Item item) {
        return ITEM_REGISTRY.getKey(item).getPath();
    }

    @ParametersAreNonnullByDefault
    public static Item getItem(ResourceLocation location) {
        return ITEM_REGISTRY.get(location);
    }
}

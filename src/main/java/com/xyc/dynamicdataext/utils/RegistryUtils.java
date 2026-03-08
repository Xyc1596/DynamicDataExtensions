package com.xyc.dynamicdataext.utils;

import com.xyc.dynamicdataext.base.DynamicTagEntry;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import javax.annotation.Nullable;
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
        for (Holder<T> holder : holders)
            output.add(holder.value());
        return output;
    }

    @ParametersAreNonnullByDefault
    public static Holder<Item> getItemHolder(Item item) {
        return item.getDefaultInstance().getItemHolder();
    }

    @SuppressWarnings("unused")
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

    @ParametersAreNonnullByDefault
    public static Optional<HolderSet.Named<Item>> getItemTagContentsOptional(ResourceLocation location) {
        return ITEM_REGISTRY.getTag(createItemTag(location));
    }

    @ParametersAreNonnullByDefault
    public static @Nullable HolderSet.Named<Item> getItemTagContents(ResourceLocation location) {
        return getItemTagContentsOptional(location).orElse(null);
    }

    @ParametersAreNonnullByDefault
    public static Optional<HolderSet.Named<Item>> getItemTagContentsOptional(TagKey<Item> tag) {
        return ITEM_REGISTRY.getTag(tag);
    }

    @ParametersAreNonnullByDefault
    public static @Nullable HolderSet.Named<Item> getItemTagContents(TagKey<Item> tag) {
        return getItemTagContentsOptional(tag).orElse(null);
    }

    /**
     * 不包括命名空间
     */
    @SuppressWarnings("unused")
    @ParametersAreNonnullByDefault
    public static Optional<String> getItemIdOptional(Item item) {
        return Optional.ofNullable(getItemId(item));
    }

    /**
     * 不包括命名空间
     */
    @ParametersAreNonnullByDefault
    public static @Nullable String getItemId(Item item) {
        ResourceLocation location = ITEM_REGISTRY.getKey(item);
        return location.equals(ITEM_REGISTRY.getDefaultKey()) ? null : location.getPath();
    }

    @SuppressWarnings("unused")
    @ParametersAreNonnullByDefault
    public static Optional<ResourceLocation> getItemLocationOptional(Item item) {
        return Optional.ofNullable(getItemLocation(item));
    }

    @ParametersAreNonnullByDefault
    public static @Nullable ResourceLocation getItemLocation(Item item) {
        ResourceLocation location = ITEM_REGISTRY.getKey(item);
        return location.equals(ITEM_REGISTRY.getDefaultKey()) ? null : location;
    }

    @ParametersAreNonnullByDefault
    public static Optional<Item> getItemOptional(ResourceLocation location) {
        return Optional.ofNullable(getItem(location));
    }

    @ParametersAreNonnullByDefault
    public static @Nullable Item getItem(ResourceLocation location) {
        Item item = ITEM_REGISTRY.get(location);
        return item == Items.AIR ? null : item;
    }

    @SuppressWarnings("unused")
    @ParametersAreNonnullByDefault
    public static <T> Optional<String> getHolderIdOptional(Holder<T> holder) {
        return holder.unwrapKey().map(resourceKey -> resourceKey.location().getPath());
    }

    @ParametersAreNonnullByDefault
    public static <T> Optional<ResourceLocation> getHolderLocationOptional(Holder<T> holder) {
        return holder.unwrapKey().map(ResourceKey::location);
    }
}

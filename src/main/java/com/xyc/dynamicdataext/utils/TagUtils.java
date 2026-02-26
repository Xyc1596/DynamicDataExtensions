package com.xyc.dynamicdataext.utils;

import com.xyc.dynamicdataext.base.DynamicTagEntry;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.LinkedHashSet;
import java.util.Set;

public final class TagUtils {
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
    public static DynamicTagEntry<Item> createItemTagEntry(TagKey<Item> tag, Iterable<Item> contents) {
        Set<Holder<Item>> holders = new LinkedHashSet<>();
        for (Item item: contents)
            holders.add(getItemHolder(item));
        return DynamicTagEntry.itemTag(tag, holders);
    }
}

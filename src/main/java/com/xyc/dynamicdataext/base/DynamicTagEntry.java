package com.xyc.dynamicdataext.base;

import com.xyc.dynamicdataext.utils.RegistryUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.LinkedHashSet;
import java.util.Set;

public class DynamicTagEntry<T> {
    protected final Registry<T> registry;
    protected final TagKey<T> tag;
    protected final Set<Holder<T>> holders = new LinkedHashSet<>();

    public DynamicTagEntry(Registry<T> registry, TagKey<T> tag, Iterable<Holder<T>> holders) {
        this.registry = registry;
        this.tag = tag;
        for (Holder<T> h : holders) {
            this.holders.add(h);
        }
    }

    public TagKey<T> getTag() {
        return this.tag;
    }

    public Set<Holder<T>> getHolders() {
        return this.holders;
    }

    public static DynamicTagEntry<Item> itemTag(TagKey<Item> tag, Iterable<Holder<Item>> holders) {
        return new DynamicTagEntry<>(RegistryUtils.ITEM_REGISTRY, tag, holders);
    }

    @Override
    public int hashCode() {
        return this.tag.hashCode();
    }
}

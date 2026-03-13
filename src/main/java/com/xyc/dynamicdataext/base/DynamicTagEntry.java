package com.xyc.dynamicdataext.base;

import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;

import java.util.LinkedHashSet;
import java.util.Set;

public class DynamicTagEntry<T> {
    protected final TagKey<T> tag;
    protected final Set<Holder<T>> holders = new LinkedHashSet<>();

    public DynamicTagEntry(TagKey<T> tag, Iterable<Holder<T>> holders) {
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

    @Override
    public int hashCode() {
        return this.tag.hashCode();
    }
}

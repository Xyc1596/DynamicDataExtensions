package com.xyc.dynamicdataext.utils;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.item.Item;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.LinkedHashSet;
import java.util.Set;

public final class ContentUtils {
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
}

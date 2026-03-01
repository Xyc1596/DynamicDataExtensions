package com.xyc.dynamicdataext.base;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.LinkedHashMap;
import java.util.Map;

public class SingleItemRecipeGraph {
    private final Map<Item, Item> roots = new LinkedHashMap<>();     // 目标物品对应的根物品
    private final Map<Item, Integer> counts = new LinkedHashMap<>(); // 一个根物品可切成多少个目标物品

    public void addItem(ItemStack item, Item parent) {
        this.addItem(item.getItem(), parent, item.getCount());
    }

    public void addItem(Item item, Item parent, int count) {
        this.roots.put(item, this.findRoot(parent));
        this.counts.put(item, count * this.counts.get(parent));
    }

    public Item findRoot(Item item) {
        if (this.roots.containsKey(item)) {
            Item parent = this.roots.get(item);
            return parent.equals(item) ? item : this.findRoot(parent);
        } else {
            this.roots.put(item, item);
            this.counts.put(item, 1);
            return item;
        }
    }

    public Map<Item, Map<Item, Integer>> getAllGroups() {
        Map<Item, Map<Item, Integer>> output = new LinkedHashMap<>();
        for (Map.Entry<Item, Item> entry : this.roots.entrySet()) {
            Item item = entry.getKey(), root = entry.getValue();
            output.putIfAbsent(root, new LinkedHashMap<>());
            output.get(root).put(item, this.counts.get(item));
        }
        return output;
    }

    public void clear() {
        this.roots.clear();
        this.counts.clear();
    }
}

package com.xyc.dynamicdataext.modules;

import com.xyc.dynamicdataext.DynamicDataMain;
import com.xyc.dynamicdataext.DynamicDataRegistry;
import com.xyc.dynamicdataext.base.DynamicRecipeEntry;
import com.xyc.dynamicdataext.base.Module;
import com.xyc.dynamicdataext.base.SingleItemRecipeGraph;
import com.xyc.dynamicdataext.utils.LocationUtils;
import com.xyc.dynamicdataext.utils.RegistryUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;

public class WoodCutting extends Module {
    public static final String MODULE_ID = "wood_cutting";

    public WoodCutting() {
        super(DynamicDataMain.MOD_ID, MODULE_ID);
    }

    @Override
    public @NotNull Set<DynamicRecipeEntry> gatherRecipesToAdd() {
        Set<DynamicRecipeEntry> output = new LinkedHashSet<>();
        if (DynamicDataRegistry.isModuleEnabled(DynamicDataMain.MOD_ID, ReversibleCutting.MODULE_ID)) {
            output.addAll(buildPlankRecipeGraph().createAllReversibleRecipes(this));
        } else {
            output.addAll(buildPlankRecipeGraph().createAllSequentialRecipes(this));
        }
        return output;
    }

    protected @NotNull SingleItemRecipeGraph buildPlankRecipeGraph() {
        SingleItemRecipeGraph graph = new SingleItemRecipeGraph();

        Map<String, Item> materialToLogMap = new HashMap<>();
        Optional<HolderSet.Named<Item>> logs_ = RegistryUtils.getItemTagContents(ItemTags.LOGS);
        if (logs_.isPresent()) {
            for (Holder<Item> holder : logs_.get()) {
                Item log = holder.value();
                Optional<ResourceKey<Item>> name_ = holder.unwrapKey();
                if (name_.isPresent()) {
                    String name = name_.get().location().getPath();
                    boolean stripped = name.startsWith("stripped");
                    int materialStart = stripped ? 9 : 0;
                    String material;
                    if (name.endsWith("log")) {
                        material = name.substring(materialStart, name.length() - 4);
                        if (!stripped)
                            materialToLogMap.put(material, log);
                    } else if (name.endsWith("stem")) {
                        // noinspection DuplicateExpressions
                        material = name.substring(materialStart, name.length() - 5);
                        if (!stripped)
                            materialToLogMap.put(material, log);
                    } else {
                        // noinspection DuplicateExpressions
                        material = name.endsWith("wood")
                            ? name.substring(materialStart, name.length() - 5)
                            : name.substring(materialStart, name.length() - 7);
                    }
                    graph.addConnection(materialToLogMap.get(material), log);
                }
            }
        }

        Optional<HolderSet.Named<Item>> planks_ = RegistryUtils.getItemTagContents(ItemTags.PLANKS);
        if (planks_.isPresent()) {
            for (Holder<Item> holder : planks_.get()) {
                Item plank = holder.value();
                Optional<ResourceLocation> plankLocation_ = RegistryUtils.getItemLocation(plank);
                if (plankLocation_.isEmpty())
                    continue;
                ResourceLocation plankLocation = plankLocation_.get();
                String namespace = plankLocation.getNamespace();
                String plankName = plankLocation.getPath();
                String material = StringUtils.removeEnd(plankName, "_planks");

                if (materialToLogMap.containsKey(material))
                    graph.addConnection(materialToLogMap.get(material), plank, 4);

                Consumer<Item> add = item -> graph.addConnection(plank, item);
                Consumer<Item> add2 = item -> graph.addConnection(plank, item, 2);

                RegistryUtils.getItem(LocationUtils.fromNamespaceAndPath(namespace, material + "_slab"))
                             .ifPresent(add2);
                RegistryUtils.getItem(LocationUtils.fromNamespaceAndPath(namespace, material + "_stairs"))
                             .ifPresent(add);
                RegistryUtils.getItem(LocationUtils.fromNamespaceAndPath(namespace, material + "_fence"))
                             .ifPresent(add);
                RegistryUtils.getItem(LocationUtils.fromNamespaceAndPath(namespace, material + "_fence_gate"))
                             .ifPresent(add);
            }

            graph.addConnection(Items.BAMBOO_PLANKS, Items.BAMBOO_MOSAIC);
            graph.addConnection(Items.BAMBOO_MOSAIC, Items.BAMBOO_MOSAIC_SLAB, 2);
            graph.addConnection(Items.BAMBOO_MOSAIC, Items.BAMBOO_MOSAIC_STAIRS);
        }
        return graph;
    }
}
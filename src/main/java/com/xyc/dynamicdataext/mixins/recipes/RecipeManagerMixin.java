package com.xyc.dynamicdataext.mixins.recipes;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.xyc.dynamicdataext.DynamicDataRegistry;
import com.xyc.dynamicdataext.base.IInjectingRecipes;
import com.xyc.dynamicdataext.base.RecipeEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mixin(RecipeManager.class)
public abstract class RecipeManagerMixin implements IInjectingRecipes {
    @Shadow
    private Multimap<RecipeType<?>, RecipeHolder<?>> byType = ImmutableMultimap.of();
    @Shadow
    private Map<ResourceLocation, RecipeHolder<?>> byName = ImmutableMap.of();

    @Unique
    @Override
    public int[] dynamicdataext$injectRecipes() {
        int nByType = this.byType.size();
        Pair<Set<RecipeEntry>, Set<ResourceLocation>> recipesToUpdate = DynamicDataRegistry.getAllRecipesToUpdate();
        Set<RecipeEntry> recipesToAdd = recipesToUpdate.getLeft();
        Set<ResourceLocation> recipesToRemove = recipesToUpdate.getRight();

        Set<Map.Entry<RecipeType<?>, RecipeHolder<?>>> byTypeAfterRem = this.byType
            .entries()
            .stream()
            .filter(e -> !recipesToRemove.contains(e.getValue().id()))
            .collect(Collectors.toCollection(LinkedHashSet::new));
        int nAfterRemove = byTypeAfterRem.size();

        this.byType = ImmutableMultimap.copyOf(
            (Iterable<Map.Entry<RecipeType<?>, RecipeHolder<?>>>) Stream.concat(
                byTypeAfterRem.stream(),
                recipesToAdd.stream().map(RecipeEntry::toMapEntryByType)
            ).collect(Collectors.toCollection(LinkedHashSet::new))
        );
        this.byName = ImmutableMap.copyOf(
            (Iterable<Map.Entry<ResourceLocation, RecipeHolder<?>>>) Stream.concat(
                this.byName.entrySet().stream().filter(e -> !recipesToRemove.contains(e.getKey())),
                recipesToAdd.stream().map(RecipeEntry::toMapEntryByName)
            ).collect(Collectors.toCollection(LinkedHashSet::new))
        );
        int nAfterAdd = this.byType.size();

        return new int[]{nAfterAdd - nAfterRemove, nByType - nAfterRemove};
    }
}

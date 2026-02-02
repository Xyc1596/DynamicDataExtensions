package com.xyc.dynamicdataext.mixins.recipes;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.xyc.dynamicdataext.DynamicDataRegistry;
import com.xyc.dynamicdataext.base.IInjectingRecipes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

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
        var recipesToUpdate = DynamicDataRegistry.getAllRecipesToUpdate();
        Set<RecipeHolder<Recipe<?>>> recipesToAdd = recipesToUpdate.getLeft();
        Set<ResourceLocation> recipesToRemove = recipesToUpdate.getRight();

        var byTypeAfterRem = this.byType.entries()
                                        .parallelStream()
                                        .filter(e -> !recipesToRemove.contains(e.getValue().id()))
                                        .collect(Collectors.toSet());
        int nAfterRemove = byTypeAfterRem.size();

        this.byType = ImmutableMultimap.copyOf(
            Stream.concat(
                byTypeAfterRem.parallelStream(),
                recipesToAdd.parallelStream().map(h -> Map.entry(h.value().getType(), h))
            ).collect(Collectors.toSet())
        );
        this.byName = ImmutableMap.copyOf(
            Stream.concat(
                this.byName.entrySet().parallelStream().filter(e -> !recipesToRemove.contains(e.getKey())),
                recipesToAdd.parallelStream().map(h -> Map.entry(h.id(), h))
            ).collect(Collectors.toSet())
        );
        int nAfterAdd = this.byType.size();

        return new int[]{nAfterAdd - nAfterRemove, nByType - nAfterRemove};
    }
}

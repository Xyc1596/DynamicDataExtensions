package com.xyc.practicalextensions.mixins.recipes;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.xyc.practicalextensions.PracticalExtensionRegistry;
import com.xyc.practicalextensions.base.IInjectingRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.apache.commons.lang3.tuple.Triple;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mixin(RecipeManager.class)
public abstract class RecipeManagerMixin implements IInjectingRecipe {
    @Shadow
    private Multimap<RecipeType<?>, RecipeHolder<?>> byType = ImmutableMultimap.of();
    @Shadow
    private Map<ResourceLocation, RecipeHolder<?>> byName = ImmutableMap.of();

    /**
     * @see PracticalExtensionRegistry#onServerStarting(ServerStartingEvent)
     */
    @Unique
    @Override
    public Triple<Long, Long, Long> practicalextensions$injectRecipes() {
        long t1 = System.currentTimeMillis();
        long nByType = this.byType.size();
        var recipesToUpdate = PracticalExtensionRegistry.getAllRecipesToUpdate();
        Set<RecipeHolder<Recipe<?>>> recipesToAdd = recipesToUpdate.getLeft();
        Set<ResourceLocation> recipesToRemove = recipesToUpdate.getRight();

        var byTypeAfterRem = this.byType.entries()
                                        .parallelStream()
                                        .filter(e -> !recipesToRemove.contains(e.getValue().id()))
                                        .collect(Collectors.toSet());
        long nAfterRemove = byTypeAfterRem.size();

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
        long nAfterAdd = this.byType.size();

        return Triple.of(
            nByType - nAfterRemove,
            nAfterAdd - nAfterRemove,
            System.currentTimeMillis() - t1
        );
    }
}

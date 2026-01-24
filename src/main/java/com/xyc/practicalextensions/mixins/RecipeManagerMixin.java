package com.xyc.practicalextensions.mixins;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.xyc.practicalextensions.PracticalExtensionRegistry;
import com.xyc.practicalextensions.utils.IInjectingRecipe;
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
    public Triple<Long, Long, Long> practicalextensions$injectRecipes() {
        long t1 = System.currentTimeMillis();
        long nByType = byType.size();
        var recipesToUpdate = PracticalExtensionRegistry.getAllRecipesToUpdate();
        Set<RecipeHolder<Recipe<?>>> recipesToAdd = recipesToUpdate.getLeft();
        Set<ResourceLocation> recipesToRemove = recipesToUpdate.getRight();

        var byTypeAfterRem = byType.entries()
                                   .stream()
                                   .filter(e -> !recipesToRemove.contains(e.getValue().id()))
                                   .collect(Collectors.toSet());
        long nAfterRemove = byTypeAfterRem.size();

        byType = ImmutableMultimap.copyOf(
            Stream.concat(
                byTypeAfterRem.stream(),
                recipesToAdd.stream().map(h -> Map.entry(h.value().getType(), h))
            ).collect(Collectors.toSet())
        );
        byName = ImmutableMap.copyOf(
            Stream.concat(
                byName.entrySet().stream().filter(e -> !recipesToRemove.contains(e.getKey())),
                recipesToAdd.stream().map(h -> Map.entry(h.id(), h))
            ).collect(Collectors.toSet())
        );
        long nAfterAdd = byType.size();

        return Triple.of(
            nByType - nAfterRemove,
            nAfterAdd - nAfterRemove,
            System.currentTimeMillis() - t1
        );
    }
}

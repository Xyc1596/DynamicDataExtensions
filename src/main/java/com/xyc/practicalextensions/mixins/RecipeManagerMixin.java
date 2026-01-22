package com.xyc.practicalextensions.mixins;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonElement;
import com.xyc.practicalextensions.ModMain;
import com.xyc.practicalextensions.recipe.DynamicRecipeManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {
    @Shadow
    private Multimap<RecipeType<?>, RecipeHolder<?>> byType = ImmutableMultimap.of();
    @Shadow
    private Map<ResourceLocation, RecipeHolder<?>> byName = ImmutableMap.of();

    @Inject(
        method = "apply(Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;" +
            "Lnet/minecraft/util/profiling/ProfilerFiller;)V",
        at = @At(
            value = "INVOKE",
            target = "Lcom/google/common/collect/ImmutableMap$Builder;build()Lcom/google/common/collect/ImmutableMap;",
            shift = At.Shift.AFTER
        )
    )
    private void onUpdateRecipes(
        Map<ResourceLocation, JsonElement> object,
        ResourceManager resourceManager,
        ProfilerFiller profiler,
        CallbackInfo ci
    ) {
        long t1 = System.currentTimeMillis();
        long nByType = byType.size();
        var recipesToUpdate = DynamicRecipeManager.getRecipesToUpdate();
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

        ModMain.LOGGER.info(
            "{} recipe(s) removed and {} recipe(s) added in {} ms",
            nByType - nAfterRemove, nAfterAdd - nAfterRemove, System.currentTimeMillis() - t1
        );
    }
}

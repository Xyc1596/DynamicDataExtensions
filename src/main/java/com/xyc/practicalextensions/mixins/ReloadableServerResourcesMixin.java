package com.xyc.practicalextensions.mixins;

import com.mojang.logging.LogUtils;
import com.xyc.practicalextensions.base.IInjectingRecipes;
import com.xyc.practicalextensions.base.IInjectingReloadableServerResources;
import com.xyc.practicalextensions.base.IInjectingTags;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.tags.TagManager;
import net.minecraft.world.item.crafting.RecipeManager;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ReloadableServerResources.class)
public abstract class ReloadableServerResourcesMixin implements IInjectingReloadableServerResources {
    @Shadow
    @Final
    private RecipeManager recipes;
    @Shadow
    @Final
    private TagManager tagManager;

    @Unique
    private static final Logger practicalExtensions$LOGGER = LogUtils.getLogger();

    @Unique
    @Override
    public void practicalExtensions$injectTags() {
        long t1 = System.currentTimeMillis();
        int[] tagResults = ((IInjectingTags) this.tagManager).practicalextensions$injectTags();
        long t2 = System.currentTimeMillis();
        practicalExtensions$LOGGER.info(
            "Tag(s) injected in {} ms: {} added, {} removed, {} modified",
            t2 - t1, tagResults[0], tagResults[1], tagResults[2]
        );
    }

    @Unique
    @Override
    public void practicalExtensions$injectRecipes() {
        long t1 = System.currentTimeMillis();
        int[] recipeResults = ((IInjectingRecipes) this.recipes).practicalextensions$injectRecipes();
        long t2 = System.currentTimeMillis();
        practicalExtensions$LOGGER.info(
            "Recipe(s) injected in {} ms: {} added, {} removed",
            t2 - t1, recipeResults[0], recipeResults[1]
        );
    }

    @Unique
    @Override
    public void practicalExtensions$injectData() {
        this.practicalExtensions$injectTags();
        this.practicalExtensions$injectRecipes();
    }
}

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
    public void practicalExtensions$injectDataIntoManagers() {
        long tTags1 = System.currentTimeMillis();
        int[] tagResults = ((IInjectingTags) this.tagManager).practicalextensions$injectTags();
        long tTags2 = System.currentTimeMillis();
        practicalExtensions$LOGGER.info(
            "Tag(s) injected in {} ms: {} added, {} removed, {} modified",
            tTags2 - tTags1, tagResults[0], tagResults[1], tagResults[2]
        );

        long tRecipes1 = System.currentTimeMillis();
        int[] recipeResults = ((IInjectingRecipes) this.recipes).practicalextensions$injectRecipes();
        long tRecipes2 = System.currentTimeMillis();
        practicalExtensions$LOGGER.info(
            "Recipe(s) injected in {} ms: {} added, {} removed",
            tRecipes2 - tRecipes1, recipeResults[0], recipeResults[1]
        );
    }
}
